package com.example.gameoflife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
private const val EXTRA_CELL_LIST =
    "com.example.gameoflife.cell_list"

class MainActivity : AppCompatActivity() {
    private val lifeViewModel: LifeViewModel by viewModels()
    var tempBoolList: BooleanArray = BooleanArray(400)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Creates the list of cells 400 dead cells
        lifeViewModel.loadCellList()

        //Loads the list of cells with either what was sent in the extra or an all false array
        tempBoolList = intent.getBooleanArrayExtra(EXTRA_CELL_LIST) ?: BooleanArray(400)
        lifeViewModel.loadListFromBoolArray(tempBoolList)
    }

    companion object {
        fun newIntent(packageContext: Context, cellList: BooleanArray): Intent {
            return Intent(packageContext, MainActivity::class.java).apply {
                putExtra(EXTRA_CELL_LIST, cellList)
            }
        }
    }
}