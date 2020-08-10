package com.example.tweeter_v1

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tweeter_v1.R
import com.example.tweeter_v1.R.id
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.math.RoundingMode
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.text.DecimalFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import com.ml.quaterion.noiseClassification.Recognition



//import com.tensorflow.android.audio.features.WavFile

class AudioListAdapter(private val allFiles: ArrayList<File>, private var onPlayClick1: onPlayClick) :
    RecyclerView.Adapter<AudioListAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        val list_play_btn: ImageView = itemView.findViewById(id.list_play_btn)
        val list_title: TextView = itemView.findViewById(id.list_title)
        val list_duration: TextView = itemView.findViewById(id.list_duration)
        val list_classify_btn: Button = itemView.findViewById(id.list_classify_btn)
        val list_delete_btn: ImageView = itemView.findViewById(id.list_delete_btn)

        init{
            list_play_btn.setOnClickListener(this)

            //Click listener for classify button
            list_classify_btn.setOnClickListener {View ->
                val position: Int = absoluteAdapterPosition

                //Toast.makeText(itemView.context,"You clicked on classify recording ${allFiles[position].name}", Toast.LENGTH_SHORT).show()

                val audioFilePath = allFiles[absoluteAdapterPosition].absolutePath
                Log.d("ABS_PATH", allFiles[absoluteAdapterPosition].absolutePath)


                if ( audioFilePath != null){

                    Toast.makeText(itemView.context,"This bird is here, dude...", Toast.LENGTH_SHORT).show()
//                    val result = classifyNoise(audioFilePath)
//                    result_text.text = "Predicted Noise : $result"
                }
                else{
                    Toast.makeText(itemView.context,"Add some birds, dude...", Toast.LENGTH_SHORT).show()
                }
            }

            list_delete_btn.setOnClickListener {View ->
                val position: Int = absoluteAdapterPosition
                Toast.makeText(itemView.context,"You clicked on delete recording ${position + 1}", Toast.LENGTH_SHORT).show()
                onDeleteCLick(absoluteAdapterPosition)
            }
        }

        override fun onClick(p0: View?) {
            onPlayClick1.onClickListener(
                allFiles[absoluteAdapterPosition],
                absoluteAdapterPosition
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.audio_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return allFiles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.list_title.text = allFiles[position].name
        holder.list_duration.text = getDuration(allFiles[position]).toString()
    }

    private fun getDuration(file: File): String {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(file.absolutePath)
        val durationStr =
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val timeDuration = durationStr!!.toLong()
        val minutes = (timeDuration / (1000*60)) % 60
        val seconds = (timeDuration / (1000)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun onDeleteCLick(position: Int) {
        val file: File = File(allFiles[position].absolutePath)
        file.delete()
        allFiles.removeAt(position)
        notifyItemRemoved(position)
    }

//    private fun classifySound(position: Int) {
//        try {
//            //wavFile = WavFile.openWavFile(allFiles[position])
//        }
//    }


    public interface onPlayClick {
        fun onClickListener(file: File, position: Int)
    }

    fun classifyNoise ( audioFilePath: String ): String? {

        val mNumFrames: Int
        val mSampleRate: Int
        val mChannels: Int
        var meanMFCCValues : FloatArray = FloatArray(1)

        var predictedResult: String? = "Unknown"

        var wavFile: WavFile? = null
        try {
            wavFile = WavFile.openWavFile(File(audioFilePath))
            mNumFrames = wavFile.numFrames.toInt()
            mSampleRate = wavFile.sampleRate.toInt()
            mChannels = wavFile.numChannels
            val buffer =
                Array(mChannels) { DoubleArray(mNumFrames) }

            var frameOffset = 0
            val loopCounter: Int = mNumFrames * mChannels / 4096 + 1
            for (i in 0 until loopCounter) {
                frameOffset = wavFile.readFrames(buffer, mNumFrames, frameOffset)
            }

            //trimming the magnitude values to 5 decimal digits
            val df = DecimalFormat("#.#####")
            df.setRoundingMode(RoundingMode.CEILING)
            val meanBuffer = DoubleArray(mNumFrames)
            for (q in 0 until mNumFrames) {
                var frameVal = 0.0
                for (p in 0 until mChannels) {
                    frameVal = frameVal + buffer[p][q]
                }
                meanBuffer[q] = df.format(frameVal / mChannels).toDouble()
            }


            //MFCC java library.
            val mfccConvert = MFCC()
            mfccConvert.setSampleRate(mSampleRate)
            val nMFCC = 40
            mfccConvert.setN_mfcc(nMFCC)
            val mfccInput = mfccConvert.process(meanBuffer)
            val nFFT = mfccInput.size / nMFCC
            val mfccValues =
                Array(nMFCC) { DoubleArray(nFFT) }

            //loop to convert the mfcc values into multi-dimensional array
            for (i in 0 until nFFT) {
                var indexCounter = i * nMFCC
                val rowIndexValue = i % nFFT
                for (j in 0 until nMFCC) {
                    mfccValues[j][rowIndexValue] = mfccInput[indexCounter].toDouble()
                    indexCounter++
                }
            }

            //code to take the mean of mfcc values across the rows such that
            //[nMFCC x nFFT] matrix would be converted into
            //[nMFCC x 1] dimension - which would act as an input to tflite model
            meanMFCCValues = FloatArray(nMFCC)
            for (p in 0 until nMFCC) {
                var fftValAcrossRow = 0.0
                for (q in 0 until nFFT) {
                    fftValAcrossRow = fftValAcrossRow + mfccValues[p][q]
                }
                val fftMeanValAcrossRow = fftValAcrossRow / nFFT
                meanMFCCValues[p] = fftMeanValAcrossRow.toFloat()
            }


        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: WavFileException) {
            e.printStackTrace()
        }

        predictedResult = loadModelAndMakePredictions(meanMFCCValues)

        return predictedResult

    }


    protected fun loadModelAndMakePredictions(meanMFCCValues : FloatArray) : String? {

        var predictedResult: String? = "unknown"

        //load the TFLite model in 'MappedByteBuffer' format using TF Interpreter
        val tfliteModel: MappedByteBuffer =
            FileUtil.loadMappedFile(this, getModelPath())
        val tflite: Interpreter

        /** Options for configuring the Interpreter.  */
        val tfliteOptions =
            Interpreter.Options()
        tfliteOptions.setNumThreads(2)
        tflite = Interpreter(tfliteModel, tfliteOptions)

        //obtain the input and output tensor size required by the model
        //for urban sound classification, input tensor should be of 1x40x1x1 shape
        val imageTensorIndex = 0
        val imageShape =
            tflite.getInputTensor(imageTensorIndex).shape()
        val imageDataType: DataType = tflite.getInputTensor(imageTensorIndex).dataType()
        val probabilityTensorIndex = 0
        val probabilityShape =
            tflite.getOutputTensor(probabilityTensorIndex).shape()
        val probabilityDataType: DataType =
            tflite.getOutputTensor(probabilityTensorIndex).dataType()

        //need to transform the MFCC 1d float buffer into 1x40x1x1 dimension tensor using TensorBuffer
        val inBuffer: TensorBuffer = TensorBuffer.createDynamic(imageDataType)
        inBuffer.loadArray(meanMFCCValues, imageShape)
        val inpBuffer: ByteBuffer = inBuffer.getBuffer()
        val outputTensorBuffer: TensorBuffer =
            TensorBuffer.createFixedSize(probabilityShape, probabilityDataType)
        //run the predictions with input and output buffer tensors to get probability values across the labels
        tflite.run(inpBuffer, outputTensorBuffer.getBuffer())


        //Code to transform the probability predictions into label values
        val ASSOCIATED_AXIS_LABELS = "labels.txt"
        var associatedAxisLabels: List<String?>? = null
        try {
            associatedAxisLabels = FileUtil.loadLabels(this, ASSOCIATED_AXIS_LABELS)
        } catch (e: IOException) {
            Log.e("tfliteSupport", "Error reading label file", e)
        }

        //Tensor processor for processing the probability values and to sort them based on the descending order of probabilities
        val probabilityProcessor: TensorProcessor = TensorProcessor.Builder()
            .add(NormalizeOp(0.0f, 255.0f)).build()
        if (null != associatedAxisLabels) {
            // Map of labels and their corresponding probability
            val labels = TensorLabel(
                associatedAxisLabels,
                probabilityProcessor.process(outputTensorBuffer)
            )

            // Create a map to access the result based on label
            val floatMap: Map<String, Float> =
                labels.getMapWithFloatValue()

            //function to retrieve the top K probability values, in this case 'k' value is 1.
            //retrieved values are storied in 'Recognition' object with label details.
            val resultPrediction: List<Recognition>? = getTopKProbability(floatMap);

            //get the top 1 prediction from the retrieved list of top predictions
            predictedResult = getPredictedValue(resultPrediction)

        }
        return predictedResult

    }


    fun getPredictedValue(predictedList:List<Recognition>?): String?{
        val top1PredictedValue : Recognition? = predictedList?.get(0)
        return top1PredictedValue?.getTitle()
    }

    fun getModelPath(): String {
        // you can download this file from
        // see build.gradle for where to obtain this file. It should be auto
        // downloaded into assets.
        return "model.tflite"
    }

    /** Gets the top-k results.  */
    protected fun getTopKProbability(labelProb: Map<String, Float>): List<Recognition>? {
        // Find the best classifications.
        val MAX_RESULTS: Int = 1
        val pq: PriorityQueue<Recognition> = PriorityQueue(
            MAX_RESULTS,
            Comparator<Recognition> { lhs, rhs -> // Intentionally reversed to put high confidence at the head of the queue.
                java.lang.Float.compare(rhs.getConfidence(), lhs.getConfidence())
            })
        for (entry in labelProb.entries) {
            pq.add(Recognition("" + entry.key, entry.key, entry.value))
        }
        val recognitions: ArrayList<Recognition> = ArrayList()
        val recognitionsSize: Int = Math.min(pq.size, MAX_RESULTS)
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }
        return recognitions
    }

}