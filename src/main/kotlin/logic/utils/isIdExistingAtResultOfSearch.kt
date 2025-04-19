package org.seoulsquad.logic.utils

import org.seoulsquad.logic.model.MealDate

fun isIdExistingAtResultOfSearch(id : Int, meals: List<MealDate>):Boolean{
 return meals.any { it.id==id }
}