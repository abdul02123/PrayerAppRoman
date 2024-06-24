package com.roman.application.home.presentation.dialogues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.roman.application.databinding.CityBottomSheetLayoutBinding
import com.roman.application.home.domain.model.response.city.City
import com.roman.application.home.presentation.adapter.CitiesAdapter

class BottomSheetDialogue : BottomSheetDialogFragment() {
    private lateinit var binding: CityBottomSheetLayoutBinding
    private var onClick: ((value: City) -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CityBottomSheetLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(TITLE)
        val citiesList = arguments?.getSerializable(LIST) as ArrayList<City>
        val adapter = CitiesAdapter(citiesList, onClick = {
            onClick?.invoke(it)
            dismiss()
        })
        binding.recyclerView.adapter = adapter
    }

    companion object {
        private const val TITLE = "TITLE"
        private const val LIST = "LIST"

        fun getInstance(
            title: String?,
            citiesList: ArrayList<City>,
            onClick: ((value: City) -> Unit)
        ): BottomSheetDialogue {
            val dialogue = BottomSheetDialogue()
            val bundle = Bundle()
            bundle.putSerializable(TITLE, title)
            bundle.putSerializable(LIST, citiesList)
            dialogue.arguments = bundle
            dialogue.onClick = onClick
            return dialogue
        }
    }
}