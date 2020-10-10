package com.talentnest.everything.listener

import com.talentnest.everything.categoryAdapter.Category

interface AdminCategoryListener {
    fun onItemClicked(data: Category)
}