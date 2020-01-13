package com.limesplash.gitrepobrowser.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.limesplash.gitrepobrowser.model.GithubRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

import org.junit.Assert.*

class ReposAdapterTest {

    @Test
    fun onCreateViewHolder() {
        val adapter = ReposAdapter(listOf(GithubRepo("testName","")))

        val context: Context = mockk()

        val layoutInflater: LayoutInflater = mockk()
        every { context.getSystemService(any()) } returns layoutInflater
        every { layoutInflater.inflate(any() as Int,any(),any()) } returns TextView(context)

        val viewGroup: ViewGroup = mockk()
        every { viewGroup.getContext() } returns context

        val viewHolder = adapter.onCreateViewHolder(viewGroup,0)
        assertNotNull(viewHolder)

    }


    @Test
    fun getItemCount() {
        val adapter = ReposAdapter(listOf(mockk(), mockk()))
        assertEquals(2,adapter.itemCount)
    }

    @Test
    fun onBindViewHolder() {
        val viewHolder: ReposAdapter.ViewHolder = mockk()

        val textView: TextView = mockk()

        every { textView.setText(any() as String) } returns Unit

        every { viewHolder.titleView } returns textView

        val adapter = ReposAdapter(listOf(GithubRepo("testName","")))

        adapter.onBindViewHolder(viewHolder,0)

        verify { textView.setText("testName") }

    }
}