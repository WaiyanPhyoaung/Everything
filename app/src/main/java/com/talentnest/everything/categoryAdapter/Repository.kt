package com.talentnest.everything.categoryAdapter

import com.talentnest.everything.R

class CategoryRepository {
    fun getListOfCategory(): List<Category> {
        return listOf(
            Category(R.drawable.tshirts, "TShirts"),
            Category(R.drawable.sports, "Sports"),
            Category(R.drawable.female_dresses,"female_dresses"),
            Category(R.drawable.glasses,"glasses"),
            Category(R.drawable.hats,"hats"),
            Category(R.drawable.sweather,"sweather"),
            Category(R.drawable.headphoness,"head_phones"),
            Category(R.drawable.laptops,"laptops"),
            Category(R.drawable.watches,"watches"),
            Category(R.drawable.purses_bags,"purse_bags"),
            Category(R.drawable.mobiles,"mobiles"),
            Category(R.drawable.shoess,"shoes")
        )
    }

}

class Category(
    val image: Int,
    val name: String
)