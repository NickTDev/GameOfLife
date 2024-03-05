package com.example.gameoflife

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class LifeViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val cellList = mutableListOf<MutableList<Cell>>()
    val commandList = mutableListOf<String>()
    var gridSideSize : Int = 20
    var isPaused : Boolean = true
    var aliveColor = "Red"
    var deadColor = "White"
    var animCount : Float = 0.0f

    //Translates an integer position to a Point with an X and Y coordinate system
    fun positionToPoint (position: Int) : Point{
        val newPoint = Point(position % gridSideSize,position / gridSideSize)
        return newPoint
    }

    //Returns the amount of neighbors that are alive of a given position
    fun getNumAliveNeighbors(position: Int) : Int {
        var numAlive : Int = 0
        val newPoint: Point = positionToPoint(position)

        if (getEastAlive(newPoint))
            numAlive++
        if (getSouthEastAlive(newPoint))
            numAlive++
        if (getSouthAlive(newPoint))
            numAlive++
        if (getSouthWestAlive(newPoint))
            numAlive++
        if (getWestAlive(newPoint))
            numAlive++
        if (getNorthWestAlive(newPoint))
            numAlive++
        if (getNorthAlive(newPoint))
            numAlive++
        if (getNorthEastAlive(newPoint))
            numAlive++

        return numAlive
    }

    //Returns whether or not the neighbor of the given point is alive
    private fun getEastAlive(point: Point) : Boolean {
        val newPoint : Point = Point(point.xCoord + 1, point.yCoord)

        if(newPoint.xCoord == gridSideSize)
            newPoint.xCoord = 0

        if (cellList[newPoint.xCoord][newPoint.yCoord].isAlive) {
            return true
        }
        return false
    }

    //Returns whether or not the neighbor of the given point is alive
    private fun getSouthEastAlive(point: Point) : Boolean {
        val newPoint : Point = Point(point.xCoord + 1, point.yCoord + 1)

        if(newPoint.xCoord == gridSideSize)
            newPoint.xCoord = 0
        if(newPoint.yCoord == gridSideSize)
            newPoint.yCoord = 0

        if (cellList[newPoint.xCoord][newPoint.yCoord].isAlive) {
            return true
        }
        return false
    }

    //Returns whether or not the neighbor of the given point is alive
    private fun getSouthAlive(point: Point) : Boolean {
        val newPoint : Point = Point(point.xCoord, point.yCoord + 1)

        if(newPoint.yCoord == gridSideSize)
            newPoint.yCoord = 0

        if (cellList[newPoint.xCoord][newPoint.yCoord].isAlive) {
            return true
        }
        return false
    }

    //Returns whether or not the neighbor of the given point is alive
    private fun getSouthWestAlive(point: Point) : Boolean {
        val newPoint : Point = Point(point.xCoord - 1, point.yCoord + 1)

        if(newPoint.xCoord == -1)
            newPoint.xCoord = gridSideSize - 1
        if(newPoint.yCoord == gridSideSize)
            newPoint.yCoord = 0

        if (cellList[newPoint.xCoord][newPoint.yCoord].isAlive) {
            return true
        }
        return false
    }

    //Returns whether or not the neighbor of the given point is alive
    private fun getWestAlive(point: Point) : Boolean {
        val newPoint : Point = Point(point.xCoord - 1, point.yCoord)

        if(newPoint.xCoord == -1)
            newPoint.xCoord = gridSideSize - 1

        if (cellList[newPoint.xCoord][newPoint.yCoord].isAlive) {
            return true
        }
        return false
    }

    //Returns whether or not the neighbor of the given point is alive
    private fun getNorthWestAlive(point: Point) : Boolean {
        val newPoint : Point = Point(point.xCoord - 1, point.yCoord - 1)

        if(newPoint.xCoord == -1)
            newPoint.xCoord = gridSideSize - 1
        if(newPoint.yCoord == -1)
            newPoint.yCoord = gridSideSize - 1

        if (cellList[newPoint.xCoord][newPoint.yCoord].isAlive) {
            return true
        }
        return false
    }

    //Returns whether or not the neighbor of the given point is alive
    private fun getNorthAlive(point: Point) : Boolean {
        val newPoint : Point = Point(point.xCoord, point.yCoord - 1)

        if(newPoint.yCoord == -1)
            newPoint.yCoord = gridSideSize - 1

        if (cellList[newPoint.xCoord][newPoint.yCoord].isAlive) {
            return true
        }
        return false
    }

    //Returns whether or not the neighbor of the given point is alive
    private fun getNorthEastAlive(point: Point) : Boolean {
        val newPoint : Point = Point(point.xCoord + 1, point.yCoord - 1)

        if(newPoint.xCoord == gridSideSize)
            newPoint.xCoord = 0
        if(newPoint.yCoord == -1)
            newPoint.yCoord = gridSideSize - 1

        if (cellList[newPoint.xCoord][newPoint.yCoord].isAlive) {
            return true
        }
        return false
    }

    //Returns if a point is alive based on the given position
    fun getIsAlive(position: Int) : Boolean{
        val point = positionToPoint(position)
        if (cellList[point.xCoord][point.yCoord].isAlive)
            return true

        return false
    }

    //Sets a point to be either alive or dead based on the given status and position
    fun setIsAlive(alive: Boolean, position: Int) {
        val point = positionToPoint(position)
        cellList[point.xCoord][point.yCoord].isAlive = alive
    }

    //Creates the cell list with all dead cells
    fun loadCellList() {
        var i = 0
        var j = 0

        while (i < gridSideSize) {
            val aCellList = mutableListOf<Cell>()
            cellList.add(aCellList)
            while (j < gridSideSize) {
                aCellList.add(Cell(false))
                j++
            }
            i++
            j = 0
        }
    }

    //Loads the cell list with information based on the given array of Booleans
    fun loadListFromBoolArray(list : BooleanArray) {
        var i = 0
        var j = 0
        var k = 0

        while (i < gridSideSize) {
            while (j < gridSideSize) {
                cellList[i][j].isAlive = list[k]
                j++
                k++
            }
            i++
            j = 0
        }
    }

    //Sets all cells within the cell list to be dead
    fun clearCellList() {
        var i = 0
        var j = 0

        while (i < gridSideSize) {
            while (j < gridSideSize) {
                cellList[i][j].isAlive = false
                j++
            }
            i++
            j = 0
        }
    }
}