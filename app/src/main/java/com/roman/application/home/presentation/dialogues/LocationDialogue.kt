package com.roman.application.home.presentation.dialogues

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.roman.application.databinding.LocationDialogueLayoutBinding

class LocationDialogue : DialogFragment() {

    private lateinit var binding: LocationDialogueLayoutBinding
    private var onClick:(()->Unit)?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationDialogueLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(MESSAGE)

//        binding.tvAddMessage.text = title
//        binding.imgView.setImageResource(image?:0)

        binding.tvDone.setOnClickListener{
            dismiss()
        }

        dialog?.window?.let {
            it.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            it.setGravity(Gravity.CENTER)
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
//        dialog?.setCancelable(false)
    }


    companion object{

        private const val MESSAGE = "MESSAGE"

        fun getInstance(message: String?=null, onClick:(()->Unit)?): LocationDialogue {
            val dialogue = LocationDialogue()
            val bundle = Bundle()
            bundle.putSerializable(MESSAGE, message)
            dialogue.arguments = bundle
            dialogue.onClick = onClick
            return dialogue
        }
    }
}