package com.example.tweeter_v1

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.tweeter_v1.fragments.ProfileFragment
import com.example.tweeter_v1.fragments.StatsFragment
import com.google.firebase.database.DatabaseReference
import com.ml.quaterion.noiseClassification.Recognition
import kotlinx.android.synthetic.main.classification.*
import kotlinx.android.synthetic.main.navigation_main.*
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


class ClassificationActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.classification)

        fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
            var strAdd = ""
            var geocoder = Geocoder(this, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                if (addresses != null) {
                    val returnedAddress = addresses.get(0)
                    val strReturnedAddress = StringBuilder("")
                    for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    }
                    strAdd = strReturnedAddress.toString()
                    Log.d("Address", strReturnedAddress.toString())
                } else {
                    Log.w("Address", "No Address returned!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.w("Address", "Cannot get Address!")
            }
            return strAdd
        }
        VerifyClassification.LocationHelper().startListeningUserLocation(
            this,
            object : VerifyClassification.LocationHelper.MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    // Here you got user location :)
                    //CurLoc = location.latitude.toString() + "," + location.longitude.toString()
                    CurLoc = getCompleteAddressString(location.latitude, location.longitude)
                    Log.d("Location", "" + CurLoc)
                }
            })



        /** 5 Bird Data CLASSES declared */
        val treeSwallow = Bird("Tree Swallow", R.drawable.tree_swallow, "https://en.wikipedia.org/wiki/Tree_swallow")
        val blueJay = Bird("Blue Jay", R.drawable.blue_jay, "https://en.wikipedia.org/wiki/Blue_jay")
        val osprey = Bird("Osprey", R.drawable.osprey, "https://en.wikipedia.org/wiki/Osprey")
        val cedarWaxwing = Bird("Cedar Waxwing", R.drawable.cedar_waxwing, "https://en.wikipedia.org/wiki/Cedar_waxwing")
        val greatHornedOwl = Bird("Great Horned Owl", R.drawable.great_horned_owl, "https://en.wikipedia.org/wiki/Great_horned_owl")

        var audioFilePath = intent.getStringExtra("audio_file_path").toString()
        var audioFile = intent.getStringExtra("audio_file").toString()
        Log.d("File_path", "file path is $audioFilePath")
        val textFileName = findViewById<TextView>(R.id.textViewClassificationTitle)
        textFileName.text = audioFile

        val classificationResult = findViewById<TextView>(R.id.textViewClassificationResult)

        val result = classifyNoise(audioFilePath)

        //Bird image shown according to classification result
        if (result == "Osprey") {
            imageViewClassificationImage.setImageResource(osprey.image)
        } else if (result == "Cedar Waxwing") {
            imageViewClassificationImage.setImageResource(cedarWaxwing.image)
        } else if (result == "Great Horned Owl") {
            imageViewClassificationImage.setImageResource(greatHornedOwl.image)
        } else if (result == "Tree Swallow") {
            imageViewClassificationImage.setImageResource(treeSwallow.image)
        } else if (result == "Blue Jay") {
            imageViewClassificationImage.setImageResource(blueJay.image)
        } else {
            imageViewClassificationImage.setImageResource(R.drawable.tweeter_bird)
        }

        val result_1 = "Predicted Noise: $result"
        Log.d("RESULT", result_1)
        classificationResult.text = result_1


        buttonSubmitForClassification.setOnClickListener {
            writeNewBird(dbReference, CurLoc, result.toString())
            setContentView( R.layout.navigation_main )
            onBackPressedDispatcher.addCallback { this }
            val profileFragment = ProfileFragment()
            val androidFragment = AndroidFragment()
            val bookFragment = BookFragment()
            val recorderFragment = RecorderFragment()
            val statsFragment = StatsFragment()
            supportFragmentManager.beginTransaction().apply{ replace( R.id.fl_wrapper, bookFragment ).commit()

                top_navigation.setOnNavigationItemSelectedListener {
                    when (it.itemId ){
                        R.id.ic_account -> supportFragmentManager.beginTransaction().apply{ replace( R.id.fl_wrapper, profileFragment ).commit()
                            addToBackStack(null)}
                        R.id.ic_android -> supportFragmentManager.beginTransaction().apply{ replace( R.id.fl_wrapper, androidFragment ).commit()
                            addToBackStack(null)}
                        R.id.ic_book -> supportFragmentManager.beginTransaction().apply{ replace( R.id.fl_wrapper, bookFragment ).commit()
                            addToBackStack(null)}
                        R.id.ic_recorder -> supportFragmentManager.beginTransaction().apply{ replace( R.id.fl_wrapper, recorderFragment ).commit()
                            addToBackStack(null)}
                        R.id.ic_stats -> supportFragmentManager.beginTransaction().apply{ replace( R.id.fl_wrapper, statsFragment ).commit()
                            addToBackStack(null)}
                    }
                    true
                }

            }
        }
    }

    fun classifyNoise(audioFilePath: String): String? {

        val mNumFrames: Int
        val mSampleRate: Int
        val mChannels: Int
        var meanMFCCValues: FloatArray = FloatArray(1)

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


    private fun loadModelAndMakePredictions(meanMFCCValues: FloatArray): String? {

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

            /**
             * Results and percentage assumably are inside floatMap. May be able to use this to display classification details
             */

            // Create a map to access the result based on label
            val floatMap: Map<String, Float> =
                labels.getMapWithFloatValue()
            println("\n***** FLOAT MAP DATA *****\n")
            floatMap.forEach { (String, float) -> println("\nString = $String\nFloat = $float\n") }

            //function to retrieve the top K probability values, in this case 'k' value is 1.
            //retrieved values are storied in 'Recognition' object with label details.
            val resultPrediction: List<Recognition>? = getTopKProbability(floatMap);

            //get the top 1 prediction from the retrieved list of top predictions
            predictedResult = getPredictedValue(resultPrediction)

        }
        return predictedResult

    }


    fun getPredictedValue(predictedList: List<Recognition>?): String? {
        val top1PredictedValue: Recognition? = predictedList?.get(0)
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

    fun writeNewBird(
        firebaseData: DatabaseReference,
        CurLoc: String,
        birdsType: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User: List<VerifyClassification.DBWrite> = mutableListOf(
            VerifyClassification.DBWrite("", "", "", "", "")
        )
        User.forEach {
            it.birdsType = birdsType
            it.user = user!!.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            it.id = user!!.uid
            firebaseData.child(user.uid).child(it.time).setValue(it)
        }
        Log.d("Write_Bird_Book", "$birdsType added to bird book at $currentTime from $CurLoc")
        Toast.makeText(
            this,
            "$birdsType added to bird book at $currentTime from $CurLoc",
            Toast.LENGTH_LONG
        ).show()
    }
}
