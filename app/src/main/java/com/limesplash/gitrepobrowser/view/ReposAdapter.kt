package com.limesplash.gitrepobrowser.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.limesplash.gitrepobrowser.R
import com.limesplash.gitrepobrowser.model.GithubRepo

class ReposAdapter(private val repos: List<GithubRepo> = emptyList()):RecyclerView.Adapter<ReposAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = (parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = repos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = repos[position]
       holder.titleView.text = repo.toString()
    }
}