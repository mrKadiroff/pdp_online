package com.example.pdp_online.kurs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdp_online.R
import com.example.pdp_online.database.AppDatabase
import com.example.pdp_online.databinding.FragmentEditModuleBinding
import com.example.pdp_online.databinding.FragmentKursEditBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Modul

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditModuleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditModuleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentEditModuleBinding
    lateinit var appDatabase: AppDatabase
    lateinit var modullist: List<Modul>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditModuleBinding.inflate(inflater,container,false)
        appDatabase = AppDatabase.getInstance(binding.root.context)

        setUi()
        updateModul()

        return binding.root
    }


    var same = false
    private fun updateModul() {
        val modul= arguments?.getSerializable("modu") as Modul
        val kurss= arguments?.getSerializable("qurs") as Kurs
        binding.floatingActionButton.setOnClickListener {

            binding.floatingActionButton.setOnClickListener {
                modullist = appDatabase.modulDao().getAllMainModule() as ArrayList<Modul>
                val modName = binding.modulName.text.toString().trim()

                if (binding.modulName.text.toString().isNotEmpty() && binding.modulPosition.text.toString().isNotEmpty()){
                    for (i in 0 until modullist.size){
                        if (modullist[i].mod_name == modName){
                            same = true
                            break
                        }
                    }
                    if (!same){
                        modul.mod_name = binding.modulName.text.toString()
                        modul.mod_position = binding.modulPosition.text.toString().toInt()
                        appDatabase.modulDao().updateModul(modul)
                        findNavController().popBackStack()
                    }else{
                        Toast.makeText(binding.root.context, "Bunday nomli modul bor!!", Toast.LENGTH_SHORT).show()
                        same=false
                    }
                }else{
                    Toast.makeText(binding.root.context,"Ma'lumotlarni to'liq kiritmadingizku brat",Toast.LENGTH_SHORT).show()
                }
            }










        }
    }

    private fun setUi() {
        val modul= arguments?.getSerializable("modu") as Modul
        binding.kurs.text = modul.mod_name
        binding.modulName.setText(modul.mod_name)
        binding.modulPosition.setText(modul.mod_position!!.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditModuleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditModuleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}