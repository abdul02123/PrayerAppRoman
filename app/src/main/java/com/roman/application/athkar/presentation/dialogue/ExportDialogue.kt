package com.roman.application.athkar.presentation.dialogue

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.roman.application.databinding.ExportLayoutBinding
import com.roman.application.util.enums.SelectionType

class ExportDialogue: DialogFragment() {

    private lateinit var binding: ExportLayoutBinding
    private var onClick: ((selectionType: Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExportLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(MESSAGE)

        binding.tvImage.setOnClickListener {
            onClick?.invoke(SelectionType.IMAGE.indentifier)
            dismiss()
        }

        binding.tvVideo.setOnClickListener {
            onClick?.invoke(SelectionType.VIDEO.indentifier)
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


    companion object {

        private const val MESSAGE = "MESSAGE"

        fun getInstance(
            message: String? = null,
            onClick: ((selectionType: Int) -> Unit)?
        ): ExportDialogue {
            val dialogue = ExportDialogue()
            val bundle = Bundle()
            bundle.putSerializable(MESSAGE, message)
            dialogue.arguments = bundle
            dialogue.onClick = onClick
            return dialogue
        }
    }
}