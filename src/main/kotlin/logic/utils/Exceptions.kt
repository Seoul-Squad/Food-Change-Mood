package logic.utils

class NoMealsFoundException(message: String) : Throwable(message)




open class InvalidDateException(message: String) : Exception(message)

class InvalidDateForSearchException :
    InvalidDateException("Invalid date for search please enter format like that MM-DD-YYYY")

class InvalidSearchException : InvalidDateException("No meals found")


class InvalidIdException : Exception("Invalid id")
