package com.example.pdp_online.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pdp_online.R
import com.example.pdp_online.adapter.LessonHorizAdapter
import com.example.pdp_online.database.AppDatabase
import com.example.pdp_online.databinding.FragmentDarsBinding
import com.example.pdp_online.databinding.FragmentHomeBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Lesson
import com.example.pdp_online.entity.Modul
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DarsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DarsFragment : Fragment() {
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
    lateinit var binding: FragmentDarsBinding
    lateinit var appDatabase: AppDatabase
    private lateinit var lessonHorizAdapter: LessonHorizAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDarsBinding.inflate(layoutInflater,container,false)
        appDatabase = AppDatabase.getInstance(binding.root.context)


        setRv()



        val moduler = arguments?.getSerializable("horizon") as Modul
        binding.teksss.text = moduler.mod_name



        return binding.root
    }

    private fun setRv() {
        val moduler = arguments?.getSerializable("horizon") as Modul
        binding.teksss.text = moduler.mod_name

        lessonHorizAdapter = LessonHorizAdapter(object:LessonHorizAdapter.OnItemClickListener{
            override fun onItemClick(lesson: Lesson) {
                var bundle = Bundle()
                bundle.putSerializable("dars",lesson)
                findNavController().navigate(R.id.lessonChildFragment,bundle)
            }

        })
        appDatabase.lessonDao().getLessonByModuleId(moduler.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Consumer<List<Lesson>> {
                override fun accept(t: List<Lesson>?) {
                    lessonHorizAdapter.submitList(t)
                }

            }, object : Consumer<Throwable> {
                override fun accept(t: Throwable?) {

                }

            })
        binding.rv.setLayoutManager(GridLayoutManager(binding.root.context, 3))
        binding.rv.adapter = lessonHorizAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DarsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DarsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}