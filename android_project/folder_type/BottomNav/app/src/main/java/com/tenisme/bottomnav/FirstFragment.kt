package com.tenisme.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FirstFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private var textView: TextView? = null
    private var text = "안녕하세요??"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(
            R.layout.fragment_first,
            container, false
        ) as ViewGroup

        textView = rootView.findViewById(R.id.textViewFirst)

        textView!!.text = text

        textView!!.setOnClickListener(View.OnClickListener {
            text = "반갑습니다~~"
            textView!!.text = text
        })

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}