package com.example.gameoflife

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gameoflife.databinding.FragmentLifeBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class LifeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val lifeViewModel: LifeViewModel by activityViewModels()

    private var _binding: FragmentLifeBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val lifeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentLifeBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this.context, 20)
        recyclerView.adapter = TTTAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            startButton.setOnClickListener {
                lifeViewModel.isPaused = false
                doStep()
            }

            pauseButton.setOnClickListener {
                lifeViewModel.isPaused = true
            }

            clearButton.setOnClickListener {
                lifeViewModel.isPaused = true
                lifeViewModel.clearCellList()
                lifeViewModel.animCount = 0.0f
                recyclerView.adapter?.notifyDataSetChanged()
            }

            whiteCheck.setOnClickListener {
                yellowCheck.isChecked = false
                purpleCheck.isChecked = false
                lifeViewModel.deadColor = "White"
                recyclerView.adapter?.notifyDataSetChanged()
            }

            yellowCheck.setOnClickListener {
                whiteCheck.isChecked = false
                purpleCheck.isChecked = false
                lifeViewModel.deadColor = "Yellow"
                recyclerView.adapter?.notifyDataSetChanged()
            }

            purpleCheck.setOnClickListener {
                yellowCheck.isChecked = false
                whiteCheck.isChecked = false
                lifeViewModel.deadColor = "Purple"
                recyclerView.adapter?.notifyDataSetChanged()
            }

            redCheck.setOnClickListener {
                blueCheck.isChecked = false
                greenCheck.isChecked = false
                lifeViewModel.aliveColor = "Red"
                recyclerView.adapter?.notifyDataSetChanged()
            }

            blueCheck.setOnClickListener {
                greenCheck.isChecked = false
                redCheck.isChecked = false
                lifeViewModel.aliveColor = "Blue"
                recyclerView.adapter?.notifyDataSetChanged()
            }

            greenCheck.setOnClickListener {
                redCheck.isChecked = false
                blueCheck.isChecked = false
                lifeViewModel.aliveColor = "Green"
                recyclerView.adapter?.notifyDataSetChanged()
            }

            saveButton.setOnClickListener {
                lifeViewModel.isPaused = true
                writeFile()
            }

            loadButton.setOnClickListener {
                lifeViewModel.isPaused = true
                lifeViewModel.animCount = 0.0f
                readGridFile()
            }

            //Creates an array of the current cell list to be sent to a new activity
            cloneButton.setOnClickListener {
                lifeViewModel.isPaused = true
                lifeViewModel.animCount = 0.0f

                var i = 0
                var j = 0
                var k = 0
                val aList : BooleanArray = BooleanArray(400)

                while (i < lifeViewModel.gridSideSize) {
                    while (j < lifeViewModel.gridSideSize) {
                        aList[k] = lifeViewModel.cellList[i][j].isAlive

                        j++
                        k++
                    }
                    i++
                    j = 0
                }

                val intent = MainActivity.newIntent(context!!, aList)
                lifeLauncher.launch(intent)
            }
        }

    }

    private inner class CellViewHolder(cellView: View): RecyclerView.ViewHolder(cellView) {
        val image: ImageView

        init {
            image = cellView.findViewById(R.id.cell_view)
            val width = Resources.getSystem().displayMetrics.widthPixels / 20
            itemView.layoutParams = ViewGroup.LayoutParams(width, width)
        }

        //Sets the cell in the position to alive and changes its drawable and color
        fun setAlive(position: Int) {
            //if (lifeViewModel.aliveColor == "Red") {
            //    image.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.red_cell, null))
            //    image.alpha = (Math.abs((Math.cos(1.57 * lifeViewModel.animCount) / 1.5)) + 0.3).toFloat()
            //}
            //else if (lifeViewModel.aliveColor == "Green") {
            //    image.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.green_cell, null))
            //    image.alpha = (Math.abs((Math.cos(1.57 * lifeViewModel.animCount) / 1.5)) + 0.3).toFloat()
            //}
            //else if (lifeViewModel.aliveColor == "Blue") {
            //    image.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.blue_cell, null))
            //    image.alpha = (Math.abs((Math.cos(1.57 * lifeViewModel.animCount) / 1.5)) + 0.3).toFloat()
            //}
            //lifeViewModel.setIsAlive(true, position)
            image.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.cell, null))
            if (lifeViewModel.aliveColor == "Red") {
                image.alpha = (Math.abs((Math.cos(1.57 * lifeViewModel.animCount) / 1.5)) + 0.3).toFloat()
                image.setColorFilter(Color.argb(100, 200, 0, 0));
            }
            else if (lifeViewModel.aliveColor == "Green") {
                image.alpha = (Math.abs((Math.cos(1.57 * lifeViewModel.animCount) / 1.5)) + 0.3).toFloat()
                image.setColorFilter(Color.argb(100, 0, 200, 0));
            }
            else if (lifeViewModel.aliveColor == "Blue") {
                image.alpha = (Math.abs((Math.cos(1.57 * lifeViewModel.animCount) / 1.5)) + 0.3).toFloat()
                image.setColorFilter(Color.argb(100, 0, 0, 200));
            }
            lifeViewModel.setIsAlive(true, position)
        }

        //Sets the cell in the position to dead and changes its drawable and color
        fun setDead(position: Int) {
            //if (lifeViewModel.deadColor == "White") {
            //    image.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.blank_square, null))
            //    image.alpha = 1.0f
            //}
            //else if (lifeViewModel.deadColor == "Yellow") {
            //    image.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.yellow_square, null))
            //    image.alpha = 1.0f
            //}
            //else if (lifeViewModel.deadColor == "Purple") {
            //    image.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.purple_square, null))
            //    image.alpha = 1.0f
            //}
            //lifeViewModel.setIsAlive(false, position)
            image.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.blank_square, null))
            if (lifeViewModel.deadColor == "White") {
                image.alpha = 1.0f
                image.setColorFilter(Color.argb(100, 200, 200, 200));
            }
            else if (lifeViewModel.deadColor == "Yellow") {
                image.alpha = 1.0f
                image.setColorFilter(Color.argb(100, 200, 200, 0));
            }
            else if (lifeViewModel.deadColor == "Purple") {
                image.alpha = 1.0f
                image.setColorFilter(Color.argb(100, 200, 0, 200));
            }
            lifeViewModel.setIsAlive(false, position)
        }

        fun getIsAlive(position: Int) : Boolean{
            return lifeViewModel.getIsAlive(position)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class TTTAdapter: RecyclerView.Adapter<CellViewHolder>() {
        final val NUM_CELLS: Int = 400
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            val cellView: View = inflater.inflate(R.layout.life_cell, parent, false)
            return CellViewHolder(cellView)
        }

        override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
            holder.image.setOnClickListener {
                if (lifeViewModel.isPaused) {
                    if (lifeViewModel.getIsAlive(position)) {
                        holder.setDead(position)
                    }
                    else
                        holder.setAlive(position)
                }
            }

            if (holder.getIsAlive(position)) {
                holder.setAlive(position)
            }
            else
                holder.setDead(position)
        }

        override fun getItemCount(): Int {
            return NUM_CELLS
        }
    }

    //Loads a list of commands based on each cell's environment
    private fun loadCommands() {
        val size = 400
        var position = 0

        while (position < size) {
            val numNeighborAlive = lifeViewModel.getNumAliveNeighbors(position)
            val currentPoint = lifeViewModel.positionToPoint(position)

            if (lifeViewModel.cellList[currentPoint.xCoord][currentPoint.yCoord].isAlive) {
                if (numNeighborAlive == 2 || numNeighborAlive == 3) {
                    lifeViewModel.commandList.add("doNothing")
                }
                else {
                    lifeViewModel.commandList.add("doDie")
                }
            }
            else {
                if (numNeighborAlive == 3) {
                    lifeViewModel.commandList.add("doLive")
                }
                else {
                    lifeViewModel.commandList.add("doNothing")
                }
            }

            position++
        }
    }

    //Updates the viewmodel's cell list based on given commands and tells the recyclerView to update
    fun updateGrid() {
        var position = 0
        for (command in lifeViewModel.commandList) {
            if (command == "doDie") {
                lifeViewModel.setIsAlive(false, position)
            }
            else if (command == "doLive") {
                lifeViewModel.setIsAlive(true, position)
            }

            position++
        }

        recyclerView.adapter?.notifyDataSetChanged()
        lifeViewModel.commandList.clear()
    }

    //Carries out each step of the generation once every second
    fun doStep() {
        loadCommands()
        updateGrid()

        object : CountDownTimer(1000, 10) {

            override fun onTick(millisUntilFinished: Long) {
                lifeViewModel.animCount += 0.1f
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFinish() {
                if (!lifeViewModel.isPaused) {
                    doStep()
                }
            }
        }.start()
    }

    //Writes the current state of the map to a given file
    private fun writeFile () {
        val directory = activity?.filesDir
        val gridFile = File(directory, binding.fileNameInput.text.toString())

        var i = 0
        var j = 0
        var toWrite = "Null"

        if (lifeViewModel.cellList[i][j].isAlive)
            toWrite = "1"

        gridFile.writeText(toWrite)
        j++

        while (i < lifeViewModel.gridSideSize) {
            while (j < lifeViewModel.gridSideSize) {
                if (lifeViewModel.cellList[i][j].isAlive)
                    toWrite = "1"
                else
                    toWrite = "0"

                gridFile.appendText("\n" + toWrite)
                j++
            }
            i++
            j = 0
        }
    }

    //Reads the state of the map from a given file
    private fun readGridFile() {
        val directory = activity?.filesDir
        val gridFile = File(directory, binding.fileNameInput.text.toString())

        if (gridFile.exists()) {
            val lineList = gridFile.readLines()
            if (lineList.isNotEmpty()) {
                var i = 0
                var j = 0
                var listCount = 0

                while (i < lifeViewModel.gridSideSize) {
                    while (j < lifeViewModel.gridSideSize) {
                        if (lineList[listCount] == "1") {
                            lifeViewModel.cellList[i][j].isAlive = true
                        }
                        else if (lineList[listCount] == "0") {
                            lifeViewModel.cellList[i][j].isAlive = false
                        }

                        j++
                        listCount++
                    }
                    i++
                    j=0
                }

            }
            recyclerView.adapter?.notifyDataSetChanged()
        } else {
            Toast.makeText(this.context, "File Does Not Exist", Toast.LENGTH_SHORT)
                .show()
        }
    }
}