package com.mohammed.chattingapp


import android.annotation.SuppressLint
import android.content.Intent

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mohammed.chattingapp.Appconstant.AppConstants
 import com.mohammed.chattingapp.Util.AppUtil
import com.mohammed.chattingapp.adapter.ChatAdapter
import com.mohammed.chattingapp.databinding.ActivityChatBinding
import com.mohammed.chattingapp.model.Chat
import com.mohammed.chattingapp.model.User


import org.json.JSONObject
import java.io.ByteArrayOutputStream

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
@Suppress("DEPRECATION")
  class chatActivity : AppCompatActivity() {
    private lateinit var storageRef: StorageReference
    lateinit var binding: ActivityChatBinding
    var firebaseUser: FirebaseUser? = null
    private var filePath: Uri? = null
    var reference: DatabaseReference? = null


    var chatList = ArrayList<Chat>()
    var topic = ""
    var storage: FirebaseStorage? = null
    private lateinit var refrence: DatabaseReference
    private val PICK_IMAGE_REQUEST: Int = 2020
    private var hisId: String? = null
    private var hisImage: String? = null
    private var chatId: String? = null
    private var myName: String? = null
    private lateinit var appUtil: AppUtil
    private lateinit var myId: String
    private lateinit var username: String
    private lateinit var reciverimage: String

    var databas: FirebaseDatabase? = null

    var userId: String? = null



    companion object {
        lateinit var senderimg: String
        lateinit var reimg: String
        lateinit var senderusername: String
        lateinit var senderId: String

        lateinit var sendimage: String
        lateinit var   mytypeimage: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        var intent = intent
        userId = intent.getStringExtra("uid")




        setContentView(view)
        senderId = FirebaseAuth.getInstance().currentUser!!.uid
        databas = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        username = intent.getStringExtra("username").toString()
        reciverimage = intent.getStringExtra("imageuri").toString()
        appUtil = AppUtil()
        myId = appUtil.getUID()!!
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }








        binding.chatRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)





        if (intent.hasExtra("chatId")) {

            chatId = intent.getStringExtra("chatId")
            hisId = intent.getStringExtra("hisId")
            hisImage = intent.getStringExtra("hisImage")


        } else {
            hisId = intent.getStringExtra("hisId")
            hisImage = intent.getStringExtra("hisImage")
        }














        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                val user = snapshot.getValue(User::class.java)
                senderimg = snapshot.child("imageuri").getValue().toString()
                senderusername = snapshot.child("username").getValue().toString()
                senderId = snapshot.child("uid").getValue().toString()
                binding.tvUserName.text = user!!.username



                if (user.imageuri == "") {
                    binding.imgProfile.setImageResource(R.drawable.goog)

                } else {


                    Glide.with(applicationContext).load(user.imageuri)
                        .fitCenter()

                        .into(binding.imgProfile)

                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }


        })


        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                reimg = user!!.imageuri

                reimg = snapshot.child("imageuri").getValue().toString()


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }


        })

        reference = FirebaseDatabase.getInstance().getReference("chatimage").child(userId!!)









        binding.btnSendMessage.setOnClickListener {
            var message: String = binding.etMessage.text.toString()

            if (message.isEmpty()) {

                Toast.makeText(applicationContext, "write something", Toast.LENGTH_LONG).show()
                binding.etMessage.setText("")
            } else {

                sendmassage(firebaseUser!!.uid, userId!!, message)
                binding.etMessage.setText("")



            }

        }
        binding.imagesend.setOnClickListener {
            val intent: Intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Image"),
                PICK_IMAGE_REQUEST
            )
        }
        readMessage(firebaseUser!!.uid, userId!!)


    }


    private fun sendmassage(senderId: String, reciverId: String, message: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().reference
        var hashmap: HashMap<String, String> = HashMap()
        hashmap.put("senderId", senderId)
        hashmap.put("reciverId", reciverId)
        hashmap.put("message", message)
        hashmap.put("type", "text")


        reference!!.child("Chat").push().setValue(hashmap)

    }


    fun readMessage(senderId: String, reciverId: String) {

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (datasnapshot: DataSnapshot in snapshot.children) {

                    val chat = datasnapshot.getValue(Chat::class.java)
mytypeimage =datasnapshot.child("image").toString()
           if (chat!!.senderId.equals(senderId) && chat.reciverId.equals(reciverId) ||
                        chat.senderId.equals(reciverId) && chat.reciverId.equals(senderId)
                    )


                        chatList.add(chat!!)




                }

                val chatAdapter = ChatAdapter(this@chatActivity, chatList)
                binding.chatRecyclerView.adapter = chatAdapter
              binding.chatRecyclerView.setHasFixedSize(true)
 chatAdapter.notifyDataSetChanged()
                binding.chatRecyclerView.scrollToPosition(chatList.size - 1);

            }



        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode != null) {
            if (data != null) {
                if (data!!.data != null) {

                    filePath = data!!.data
                    var bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                    var baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos)
                    var data =baos.toByteArray()
                    val refence =
                        storage!!.reference.child("chatimage/" + UUID.randomUUID().toString())
                    refence.putFile(filePath!!)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                refence.downloadUrl.addOnSuccessListener { uri ->


                                    val myfile = uri.toString()
                                    var reference: DatabaseReference? =
                                        FirebaseDatabase.getInstance().reference

                                    var hashmap: HashMap<String, String> = HashMap()
                                    hashmap.put("senderId", firebaseUser!!.uid)
                                    hashmap.put("image", myfile)
                                    hashmap.put("reciverId", userId!!)
                                    hashmap.put("type", "photo")


                                    reference!!.child("Chat").push().setValue(hashmap)
                                    binding.etMessage.setText("")

                                    /*task ->
                            if (task.isSuccessful) {
                                refence.downloadUrl.addOnSuccessListener {  uri->


                                    val myfile = uri.toString()
                                    val messagewText: String = binding.etMessage.text.toString()
                                    val message =
                                        Chat(firebaseUser!!.uid, userId!!, messagewText,"","")
                                    message.type = "photo"
                                    message.image = myfile
                                    binding.etMessage.setText("")
                                    val randomkey = databas!!.reference.push().key
                                    databas!!.getReference("chatimage")
                                        .child(senderId)
                                        .push()


                                        .setValue(message)
                                }
                            }*/
                                }

                                /*     try {
                                         var bitmap: Bitmap =
                                             MediaStore.Images.Media.getBitmap(contentResolver, filePath)


                                     } catch (e: IOException) {
                                         e.printStackTrace()


                                     }*/
                            }
                        }
                }
            }
        }
    }

    private fun getToken(message: String) {

        firebaseUser = FirebaseAuth.getInstance().currentUser
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid!!)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val token = snapshot.child("token").value.toString()

                    val to = JSONObject()
                    val data = JSONObject()



                    data.put("hisId", myId)
                    data.put("imageuri", reciverimage)
                    data.put("title", username)
                    data.put("message", message)
                    data.put("chatId", chatId)





                    to.put("to", token)
                    to.put("data", data)
                    sendNotification(to)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun sendNotification(to: JSONObject) {

        val request: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            AppConstants.NOTIFICATION_URL,
            to,
            Response.Listener { response: JSONObject ->

                Log.d("TAG", "onResponse: $response")
            },
            Response.ErrorListener {

                Log.d("TAG", "onError: $it")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val map: MutableMap<String, String> = HashMap()

                map["Authorization"] = "key=" + AppConstants.SERVER_KEY
                map["Content-type"] = "application/json"
                return map
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
    }




}


/*    private fun uploadImage() {

        if (filePath != null) {


            var ref: StorageReference = storageRef.child("image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!).addOnSuccessListener {



                val hashMap:  HashMap<String, String> =  HashMap()


                hashMap.put("imagessend",filePath.toString())
                refrence .setValue(hashMap as Map<String, Any>)


                Toast.makeText(applicationContext, "the image is send", Toast.LENGTH_SHORT).show()

            }
                .addOnFailureListener {

                    Toast.makeText(applicationContext, "Failed" + it.message, Toast.LENGTH_SHORT)
                        .show()

                }

        }



    }*/


