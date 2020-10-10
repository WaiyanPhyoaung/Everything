package com.talentnest.everything.categoryAdapter

import com.talentnest.everything.R

class CategoryRepository {
    fun getListOfCategory(): List<Category> {
        return listOf(
            Category(R.drawable.tshirts, "TShirts"),
            Category(R.drawable.sports, "Sports"),
            Category(R.drawable.female_dresses),
            Category(R.drawable.glasses),
            Category(R.drawable.hats),
            Category(R.drawable.sweather),
            Category(R.drawable.headphoness),
            Category(R.drawable.laptops),
            Category(R.drawable.watches),
            Category(R.drawable.purses_bags),
            Category(R.drawable.mobiles),
            Category(R.drawable.shoess)
        )
    }

}

class Category(
    val image: Int,
    val name: String
)