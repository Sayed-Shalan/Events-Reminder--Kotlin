package com.sayed.thirdway.ui.base

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class DataBindingViewHolder<Binding : ViewDataBinding> private constructor(var binding: Binding) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    protected val context: Context
        get() = binding.root.context

    constructor(parent: ViewGroup, layout: Int) : this(LayoutInflater.from(parent.context), parent, layout) {}

    constructor(inflater: LayoutInflater, parent: ViewGroup, layout: Int) : this(
        DataBindingUtil.inflate<Binding>(
            inflater,
            layout,
            parent,
            false
        )
    ) {
    }


    init {
        initListener()
    }

    protected fun initListener() {}
}
