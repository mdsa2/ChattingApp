package com.mohammed.chattingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mohammed.chattingapp.adapter.myAdapter


import com.mohammed.chattingapp.databinding.ActivityNewMessageBinding

import com.mohammed.chattingapp.model.User


class NewMessage : ReqisterActivity() {



  private  lateinit var userList :ArrayList<User>
    private lateinit var mAuth:FirebaseAuth
    lateinit var binding: ActivityNewMessageBinding
private lateinit var mUserDetiels:User

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Select User"
userList= ArrayList()






        getimgUserList()









        var userAdapter=myAdapter(this,userList)
        binding.recyclerView.adapter=userAdapter
 binding.recyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.imgicon.setOnClickListener {
            val intent= Intent(this@NewMessage,profile::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anime_left,R.anim.anime_left_out)
        }
/*binding.imgBack.setOnClickListener {
onBackPressed()
}*/

        getUserList()
mAuth =FirebaseAuth.getInstance()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)


    }
    fun getUserList(){

        val firebaseUser:FirebaseUser=   FirebaseAuth.getInstance().currentUser!!

        val databaseReference:DatabaseReference =FirebaseDatabase.getInstance().getReference("Users")
         databaseReference.addValueEventListener(object :ValueEventListener {

             override fun onCancelled(error: DatabaseError) {
                 Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()

             }

             override fun onDataChange(snapshot: DataSnapshot) {
                 userList.clear()










/*Toast.makeText(this@NewMessage,"this is my image url ${reimg}",Toast.LENGTH_LONG).show()*/


                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val user = datasnapshot.getValue(User::class.java)
                 if (!user!!.uid.equals(firebaseUser.uid)) {



                        userList.add(user!!)

                    }
                }
           val userAdapter = myAdapter(this@NewMessage,userList)
           binding.recyclerView.adapter=userAdapter
           }


        })
     }

    fun getimgUserList(){

        val firebaseUser:FirebaseUser=   FirebaseAuth.getInstance().currentUser!!
        val databaseReference:DatabaseReference =FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        databaseReference.addValueEventListener(object :ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()

            }
            @SuppressLint("CheckResult")
            override fun onDataChange(snapshot: DataSnapshot) {


                val user = snapshot.getValue(User::class.java)
                binding.title.setText("welcome ${user!!.username}")
                var sendimg = snapshot.child("imageuri").getValue().toString()
        Glide.with(applicationContext)
            .load(user!!.imageuri)
            .centerCrop()
            .into(binding.imgicon)
            }

        })
    }


}

