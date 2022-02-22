package com.example.pdp_online.kurs

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdp_online.R
import com.example.pdp_online.adapter.ModuleAdapter
import com.example.pdp_online.database.AppDatabase
import com.example.pdp_online.databinding.DeleteDialogBinding
import com.example.pdp_online.databinding.FragmentAddModuleBinding
import com.example.pdp_online.databinding.FragmentSettingsBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Modul
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Add_ModuleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Add_ModuleFragment : Fragment() {
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
    lateinit var binding: FragmentAddModuleBinding
    lateinit var appDatabase: AppDatabase
    private lateinit var moduleAdapter: ModuleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddModuleBinding.inflate(inflater,container,false)

        appDatabase = AppDatabase.getInstance(binding.root.context)

        addTodb()
        setRv()

        return binding.root
    }

    private fun setRv() {
        val course = arguments?.getSerializable("shit") as Kurs

        moduleAdapter = ModuleAdapter(course,object:ModuleAdapter.OnItemClickListener{
            override fun onItemClick(modul: Modul) {
                var bundle = Bundle()
                bundle.putSerializable("kursi",course)
                bundle.putSerializable("modar",modul)
                findNavController().navigate(R.id.lessonFragment,bundle)
            }

            override fun onItemDeleteClick(modul: Modul) {
                val alertDialog = AlertDialog.Builder(binding.root.context)
                val dialog = alertDialog.create()
                val dialogView = DeleteDialogBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    null,
                    false
                )
                dialogView.matn.setText("Bu modul ichida darslar kiritilgan. darslar bilan birgalikda o'chib ketishiga rozimisiz?")

                dialogView.ha.setOnClickListener {
                    appDatabase.modulDao().deleteModul(modul)
                    dialog.dismiss()
                }

                dialogView.yoq.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setView(dialogView.root)
                dialog.show()
            }

            override fun onItemEditClick(modul: Modul) {
                var bundle = Bundle()
                bundle.putSerializable("qurs",course)
                bundle.putSerializable("modu",modul)
                findNavController().navigate(R.id.editModuleFragment,bundle)
            }

        })




        appDatabase.modulDao().getModuleByKursId(course.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Consumer<List<Modul>> {
                override fun accept(t: List<Modul>?) {
                    moduleAdapter.submitList(t)
                }

            }, object : Consumer<Throwable> {
                override fun accept(t: Throwable?) {

                }

            })
        binding.rv.adapter = moduleAdapter

    }

    private fun addTodb() {
        val course = arguments?.getSerializable("shit") as Kurs
        binding.kurs.text = course.kr_name

        binding.floatingActionButton.setOnClickListener {
            val modul = Modul()
            modul.mod_name = binding.modulName.text.toString().trim()
            modul.mod_position = binding.modulPosition.text.toString()
            modul.mod_image = course.kr_image
            modul.mod_course = course.id
            Observable.fromCallable {
                appDatabase.modulDao().addModul(modul)
            }.subscribe{
                Toast.makeText(binding.root.context, "added", Toast.LENGTH_SHORT).show()
                binding.modulName.setText("")
                binding.modulPosition.setText("")
            }
            binding.kurs.setText("")

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Add_ModuleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Add_ModuleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}