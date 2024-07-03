package com.roman.application.base.dialogue

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.roman.application.databinding.ProgressLayoutBinding

class ProgressDialogue : DialogFragment() {

    private lateinit var binding: ProgressLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProgressLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(MESSAGE)

        dialog?.window?.let {
            it.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            it.setGravity(Gravity.CENTER)
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
        dialog?.setCancelable(false)
    }



    companion object {

        private const val MESSAGE = "MESSAGE"

        fun getInstance(
            message: String? = null,
        ): ProgressDialogue {
            val dialogue = ProgressDialogue()
            val bundle = Bundle()
            bundle.putSerializable(MESSAGE, message)
            dialogue.arguments = bundle
            return dialogue
        }
    }
}