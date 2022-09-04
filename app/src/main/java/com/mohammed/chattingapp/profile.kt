package com.mohammed.chattingapp




import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mohammed.chattingapp.databinding.ActivityProfileBinding
import com.mohammed.chattingapp.model.User
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
  class profile : AppCompatActivity() {

    private var filePath: Uri? = null

   private lateinit var refrence: DatabaseReference
    private var imageuri: Uri? = null
    private lateinit var storage: FirebaseStorage
    private val PICK_IMAGE_REQUEST: Int = 2020
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityProfileBinding
    private  lateinit  var storageRef: StorageReference

    private  lateinit  var uid :String
private lateinit var yourBitmap:Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.btnSave.setOnClickListener {
            uploadImage()


            binding.progressBar.visibility = View.VISIBLE


        }

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.userImage.setOnClickListener {
            chooseImage()
        }

/*

var firebaseUser = FirebaseAuth.getInstance().currentUser!!
        databaseReference =
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.uid)

*/

        var fuser: FirebaseUser?= FirebaseAuth.getInstance().currentUser

        refrence = FirebaseDatabase.getInstance().getReference("Users").child(fuser?.uid!!)
        refrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var user = snapshot.getValue(User::class.java)

                binding.etUserName.setText(user?.username)
binding.etemail.setText(user?.emailedt)
           if (user!!.imageuri == "") {

                    binding.userImage.setImageResource(R.drawable.goog)
                } else {
                    Glide.with(applicationContext).load(user.imageuri).placeholder(R.drawable.goog).into(binding.userImage)




                }





                // binding.etUserName.setText(user!!.username)
                binding.btnSave.visibility = View.VISIBLE

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@profile, "some thing wrong", Toast.LENGTH_LONG).show()

            }





        })
    }           /*if (user != null) {



                   }else {
                       Toast.makeText(this@profile, "no snapshot", Toast.LENGTH_LONG).show()
                   }*/

                       /*  val user = snapshot.getValue(User::class.java)
                       binding.etUserName.setText(user!!.username)*/

                       /*



                                         }
                                        }*/



    private   fun chooseImage() {

        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent. ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
      
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode != null) {
            filePath = data!!.data

            try {
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.userImage.setImageBitmap(bitmap)

         binding.btnSave.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()


            }
        }
    }


  private fun uploadImage() {

        if (filePath != null) {


            var ref: StorageReference = storageRef.child("image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!).addOnSuccessListener {



                val hashMap:HashMap<String,String> = HashMap()
                 hashMap.put("username", binding.etUserName.text.toString() )
                hashMap.put("imageuri",filePath.toString())
                refrence .updateChildren(hashMap as Map<String, Any>)

                binding.progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
                binding.btnSave.visibility = View.GONE
                }
                .addOnFailureListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Failed" + it.message, Toast.LENGTH_SHORT)
                        .show()

                }

        }



  }

  /*  private fun getUserInfo(onComplete:(User)->Unit){
        databaseReference.get().addOnSuccessListener {
            onComplete(it.getValue(User::class.java)!!)

        }*/
    }




/* fun UploadImageTocload() {

      if (imageuri !=null) {
          var progressDilaog = ProgressDialog(this)
          progressDilaog.setTitle("uploading")
          progressDilaog.show()
          val filename = UUID.randomUUID().toString()

          var imageRef: StorageReference =
              FirebaseStorage.getInstance().reference.child("/images/$filename")
          imageRef.putFile(imageuri!!)
              .addOnSuccessListener { p0 ->
                  progressDilaog.dismiss()
                  Toast.makeText(applicationContext, "file uploading", Toast.LENGTH_LONG).show()

                  saveuserToFirebaseDatabase(imageuri = String())
              }
              .addOnFailureListener { p0 ->
                  progressDilaog.dismiss()
                  Toast.makeText(applicationContext, p0.message, Toast.LENGTH_LONG).show()
              }
      }

 }*/







