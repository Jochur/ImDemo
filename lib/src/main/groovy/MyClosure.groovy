def aClosure = {
    String param1,Integer param2 -> println param1+param2
}

aClosure.call("this is closure",100)
