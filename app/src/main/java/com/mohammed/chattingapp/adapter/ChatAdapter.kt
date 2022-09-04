@file:Suppress("DEPRECATION")

package com.mohammed.chattingapp.adapter


import android.content.Context
import android.content.Intent.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mohammed.chattingapp.R
import com.mohammed.chattingapp.chatActivity.Companion.reimg
import com.mohammed.chattingapp.chatActivity.Companion.senderimg
import com.mohammed.chattingapp.databinding.ItemLeftBinding
import com.mohammed.chattingapp.model.Chat


class ChatAdapter(private val context: Context, private var chatList: ArrayList<Chat>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var reference: DatabaseReference? = null

    private val MESSAGE_TYPE_RIGHT = 0
    private val MESSAGE_TYPE_LEFT = 1
    var firebaseUser: FirebaseUser? = null
    var profileimage: String? = null
    var type: String? = null
    var intent = getIntent("imageuri")
    var reciverimage = intent.getStringExtra("imageuri").toString()


    var userId1 = intent.getStringExtra("uid")
    var username = intent.getStringExtra("username")



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


      return  if (viewType == MESSAGE_TYPE_LEFT) {

            val view = LayoutInflater.from(context).inflate(R.layout.item_left, parent, false)

 senderViewhiolder(view)
        } else {

            val view =
                LayoutInflater.from(context).inflate(R.layout.item_right, parent, false)
             reciverViewholder(view)
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mychat = chatList[position]
        val chat =mychat.message

        var chae1 = mychat.type
        var chae2 = mychat.image
        if (holder.javaClass == senderViewhiolder::class.java) {


            val viewHolder = holder as senderViewhiolder

            if (mychat.type.equals("photo")){

                viewHolder.binding.image.visibility = View.VISIBLE

                viewHolder.binding.tvMessage.visibility = View.GONE
                Glide.with(context)
                    .load(  chae2)
                    .placeholder(R.drawable.goog)


                    .into(viewHolder.binding.image)
                viewHolder.binding.tvMessage.setOnClickListener {
                    Toast.makeText(
                        context,
                        "this is type text ${mychat.image} ",
                        Toast.LENGTH_SHORT
                    ).show()
                }





            }else{


                viewHolder.binding.image.visibility = View.GONE

                viewHolder.binding.tvMessage.visibility = View.VISIBLE
                viewHolder.binding.tvMessage.text = mychat.message
            }

 /*           if (mychat.type.equals("photo")) {
                Toast.makeText(context, "the image is uploading ${mychat.type}", Toast.LENGTH_SHORT)
                    .show()
                viewHolder.senderimg.visibility = View.VISIBLE
                viewHolder.sendermessage.visibility = View.GONE
                Glide.with(context)
                    .load(mytypeimage)
                    .placeholder(R.drawable.goog)


                    .into(viewHolder.senderimg)

            }
            viewHolder.senderimg.visibility = View.GONE
            viewHolder.sendermessage.visibility = View.VISIBLE
            viewHolder.sendermessage.text = mychat.message*/





            viewHolder.binding.userImage.setOnClickListener {

                Toast.makeText(context, "this is my username ${mychat.type}", Toast.LENGTH_LONG)
                    .show()


            }
            Glide.with(context).load(senderimg)

                .placeholder(R.drawable.goog)


                .into(viewHolder.binding.userImage)

        } else {

            val viewHolder = holder as reciverViewholder



            if (mychat.type.equals("photo")){

                viewHolder.binding.image.visibility = View.VISIBLE

                viewHolder.binding.tvMessage.visibility = View.GONE

                    Glide.with(context)
                        .load(  mychat.image)
                        .placeholder(R.drawable.goog)


                        .into(viewHolder.binding.image)


                viewHolder.binding.image.setOnClickListener {
                    Toast.makeText(
                        context,
                        "this is type text ${mychat.image} ",
                        Toast.LENGTH_SHORT
                    ).show()
                }





            }else{


                viewHolder.binding.image.visibility = View.GONE

                viewHolder.binding.tvMessage.visibility = View.VISIBLE
                viewHolder.binding.tvMessage.text = mychat.message
            }


                        /*          Toast.makeText(
                                    context,
                                    "there is snapshot ",
                                    Toast.LENGTH_SHORT
                                ).show()
*/
                        /*                         val mychat = snapshot.getValue(Chat::class.java)
                                    type = snapshot.child("type").getValue().toString()


                                                if (mychat!!.type.equals("text")) {
                               holder.reciveim.visibility = View.GONE

                                        holder.recivermessage.visibility = View.VISIBLE

                                        holder.recivermessage.text = chat.message


                                                    Toast.makeText(
                                                        context,
                                                        "this is type image ${mychat!!.type} ",
                                                        Toast.LENGTH_SHORT
                                                    ).show()


                                    } else {


                                        holder.reciveim.visibility = View.VISIBLE

                                        holder.recivermessage.visibility = View.GONE


                                        Glide.with(context)
                                            .load("https://firebasestorage.googleapis.com/v0/b/chattingapp-df198.appspot.com/o/chatimage%2Fb4553324-8a25-49c7-b295-28bae064758b?alt=media&token=d839ea91-630e-4fff-aafe-b24aeee3d1a1")
                                            .placeholder(R.drawable.goog)


                                            .into(holder.reciveim)
                                        viewHolder.reciveim.setOnClickListener {
                                            Toast.makeText(
                                                context,
                                                "this is type image ${mychat!!.type} ",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    }


                            } else {
                                Toast.makeText(
                                    context,
                                    "there is no snapshot",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }*/



                        /*       Toast.makeText(
                                    context,
                                    "there is no snapshot ",
                                    Toast.LENGTH_SHORT
                                ).show()*/














            Glide.with(context).load(reimg)


                .into(viewHolder.binding.userImage)


        }
    }

    override fun getItemCount(): Int =chatList.size






    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == firebaseUser!!.uid) {

            return MESSAGE_TYPE_RIGHT

        } else {

            return MESSAGE_TYPE_LEFT
        }

    }

   /* fun Loadmyprofile() {
        reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid!!)

        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    Toast.makeText(
                        context,
                        "this is me username ${user!!.username}",
                        Toast.LENGTH_LONG
                    ).show()

                    Toast.makeText(context, "there snapshot", Toast.LENGTH_LONG).show()
                    profileimage = snapshot.child("imageuri").getValue().toString()




                    Glide.with(context).load(profileimage).placeholder(R.drawable.goog)
                        .into(holder.recveimag)


                } else {
                    Toast.makeText(context, "there is no snapshot", Toast.LENGTH_LONG).show()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }*/
   inner class senderViewhiolder(view: View) : RecyclerView.ViewHolder(view) {
       var binding: ItemLeftBinding = ItemLeftBinding.bind(view)
   }

    inner  class reciverViewholder(view: View) : RecyclerView.ViewHolder(view) {

        var binding: ItemLeftBinding = ItemLeftBinding.bind(view)
    }
    init {
        if (chatList !=null ){
            this.chatList = chatList
        }
    }
}
