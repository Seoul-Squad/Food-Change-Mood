package org.seoulsquad.logic.utils

import org.seoulsquad.logic.useCase.model.MealDate

fun isIdExistingAtResultOfSearch(id : Int, meals: List<MealDate>):Boolean{
 return meals.any { it.id==id }
}