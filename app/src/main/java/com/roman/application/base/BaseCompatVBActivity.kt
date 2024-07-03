package com.roman.application.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.roman.application.base.dialogue.ProgressDialogue

abstract class BaseCompatVBActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val mBinding get() = _binding
    private var progressDialog: ProgressDialogue? = null

    abstract fun setUpViewBinding(layoutInflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = setUpViewBinding(layoutInflater)
        setContentView(_binding?.root)
        init()
    }

    abstract fun init()


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun showProgressDialogue(){
        progressDialog?.dismiss()
        progressDialog = ProgressDialogue.getInstance(message = "")
        progressDialog?.show(supportFragmentManager, null)
    }

    fun hideProgressDialogue(){
        progressDialog?.dismiss()
    }


}