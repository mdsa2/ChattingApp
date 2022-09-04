package com.mohammed.chattingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohammed.chattingapp.R
import com.mohammed.chattingapp.chatActivity


import com.mohammed.chattingapp.databinding.UserRowItemBinding
import com.mohammed.chattingapp.model.User


import de.hdodenhof.circleimageview.CircleImageView


class myAdapter(private val context:Context,private val userList: ArrayList<User>): RecyclerView.Adapter<myAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

return MyViewHolder(UserRowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
val user=userList[position]
        holder.textusername.text=user.username

   Glide.with(context).load( user.imageuri).placeholder(R.drawable.goog)


            .into(holder.imguser)

holder.layoutUser.setOnClickListener {
    val intent= Intent(context,chatActivity::class.java)
    intent.putExtra("uid",user.uid)
    intent.putExtra("username",user.username)
    intent.putExtra("imageuri",user.imageuri)


    context.startActivity(intent)

}

    }

    override fun getItemCount(): Int {
        return userList.size


     }
    class MyViewHolder(val binding:UserRowItemBinding):RecyclerView.ViewHolder(binding.root) {
        val textusername:TextView=binding.firstName
 
        val imguser:CircleImageView=binding.imageView
        val layoutUser:LinearLayout=binding.layoutUser
    }
}