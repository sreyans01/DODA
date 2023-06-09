package com.sreyans.discussondrawings.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.android.autelsdk.util.Resource
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sreyans.discussondrawings.helper.Constants
import com.sreyans.discussondrawings.helper.Utils
import com.sreyans.discussondrawings.model.Drawing
import com.sreyans.discussondrawings.model.Marker
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class DrawingsRepositoryImpl(
    storageReference: StorageReference = FirebaseStorage.getInstance().reference.child(Constants.KEY_DRAWINGS),
    databaseReference: DatabaseReference = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_REFERENCE).reference,
) : DrawingsRepository {

    val storageReference = storageReference
    val databaseReference = databaseReference
    var drawingsList: ArrayList<Drawing> = ArrayList()


    override suspend fun uploadDrawing(
        title: String,
        imageUrl: String,
        markers: ArrayList<Marker>,
    ): MutableLiveData<Resource<String>> {
        val sdf = SimpleDateFormat(Constants.DATE_PATTERN_WITH_SECONDS)
        val currentDateAndTime: String = sdf.format(Date())
        val drawing = Drawing(imageUrl, title, currentDateAndTime, markers)
        var uploadDrawingResult: MutableLiveData<Resource<String>> = MutableLiveData()
        val uploadId = drawing.createdOn
        if (uploadId == null) {
            uploadDrawingResult.postValue(Resource.error(Constants.UNKNOWN_ERROR, ""))
        }
        else {
            databaseReference.child(Constants.KEY_DRAWINGS).child(uploadId).setValue(drawing)
                .addOnSuccessListener {
                    uploadDrawingResult.postValue(Resource.success("", Constants.SUCCESS))
                }.addOnFailureListener { e: Exception ->
                    uploadDrawingResult.postValue(Resource.error(e.toString(), ""))
                }.addOnCanceledListener {
                    uploadDrawingResult.postValue(Resource.error(Constants.OPERATION_CANCELLED, ""))
                }
        }
        return uploadDrawingResult
    }

    override suspend fun updateDrawing(
        drawing: Drawing
    ): MutableLiveData<Resource<String>> {
        val uploadId = drawing.createdOn
        var uploadDrawingResult: MutableLiveData<Resource<String>> = MutableLiveData()
        if (uploadId == null) {
            uploadDrawingResult.postValue(Resource.error(Constants.UNKNOWN_ERROR, ""))
        }
        else {
            databaseReference.child(Constants.KEY_DRAWINGS).child(uploadId).setValue(drawing)
                .addOnSuccessListener {
                    uploadDrawingResult.postValue(Resource.success("", Constants.SUCCESS))
                }.addOnFailureListener { e: Exception ->
                    uploadDrawingResult.postValue(Resource.error(e.toString(), ""))
                }.addOnCanceledListener {
                    uploadDrawingResult.postValue(Resource.error(Constants.OPERATION_CANCELLED, ""))
                }
        }
        return uploadDrawingResult
    }

    override suspend fun getDrawingImageUrl(selectedImageUri: Uri): MutableLiveData<Resource<String>> {
        var getDrawingImageUrlResult: MutableLiveData<Resource<String>> = MutableLiveData()
        val imageName = System.currentTimeMillis().toString() + " uploadedDrawing"
        val fileRef = storageReference.child(imageName)
        fileRef.putFile(selectedImageUri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { url ->
                getDrawingImageUrlResult.postValue(Resource.success(url.toString(), ""))
            }.addOnFailureListener { e: Exception ->
                getDrawingImageUrlResult.postValue(Resource.error(e.toString(), ""))
            }.addOnCanceledListener {
                getDrawingImageUrlResult.postValue(Resource.error(Constants.OPERATION_CANCELLED, ""))
            }
        }
            .addOnFailureListener { e: Exception ->
                getDrawingImageUrlResult.postValue(Resource.error(e.toString(), ""))
            }.addOnCanceledListener {
                getDrawingImageUrlResult.postValue(Resource.error(Constants.OPERATION_CANCELLED, ""))
            }
        return getDrawingImageUrlResult
    }

    override fun getAllDrawings(): LiveData<ArrayList<Drawing>> {
        drawingsList.clear()
        return LiveDataReactiveStreams.fromPublisher(
            Flowable.create({ emitter: FlowableEmitter<ArrayList<Drawing>> ->
                databaseReference.child(Constants.KEY_DRAWINGS).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        drawingsList.clear()
                        for (drawingSnapshots in dataSnapshot.children) {
                            val drawing: Drawing? = drawingSnapshots.getValue(Drawing::class.java)
                            drawing?.let {
                                drawingsList.add(it)
                            }
                        }
                        emitter.onNext(drawingsList)
                        emitter.onComplete()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        emitter.isCancelled()
                    }
                })
            }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
        )

    }


}