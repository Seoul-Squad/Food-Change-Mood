package logic.utils

class NoMealsFoundException : Exception("No meals found")

class InvalidDateException : Exception("Invalid date format please enter format like that MM-DD-YYYY")

class InvalidIdException : Exception("Invalid id")

class NotEnoughMealsFoundException: Exception("Not enough meals found")

class InvalidNumberException: NumberFormatException("Please enter a valid number")

class NoIngredientFoundException: Exception("No ingredient found")

class BlankInputException: Exception("Input cannot be blank")