package com.roman.application.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseCompatVBActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val mBinding get() = _binding

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


}