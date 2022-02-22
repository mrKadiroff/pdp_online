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
import com.example.pdp_online.adapter.LessonAdapter
import com.example.pdp_online.database.AppDatabase
import com.example.pdp_online.databinding.DeleteDialogBinding
import com.example.pdp_online.databinding.FragmentAddModuleBinding
import com.example.pdp_online.databinding.FragmentLessonBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Lesson
import com.example.pdp_online.entity.Modul
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
 * Use the [LessonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LessonFragment : Fragment() {
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
    lateinit var binding: FragmentLessonBinding
    lateinit var appDatabase: AppDatabase
    private lateinit var lessonAdapter: LessonAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLessonBinding.inflate(inflater,container,false)
        appDatabase = AppDatabase.getInstance(binding.root.context)

        addLesson()
        setRec()

        return binding.root
    }

    private fun setRec() {
        val modul = arguments?.getSerializable("modar") as Modul
        val course = arguments?.getSerializable("kursi") as Kurs

        lessonAdapter = LessonAdapter(course,object:LessonAdapter.OnItemClickListener{
            override fun onItemDeleteClick(lesson: Lesson) {
                val alertDialog = AlertDialog.Builder(binding.root.context)
                val dialog = alertDialog.create()
                val dialogView = DeleteDialogBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    null,
                    false
                )
                dialogView.matn.setText("Darsni o'chirishga rozimisiz?")

                dialogView.ha.setOnClickListener {
                    appDatabase.lessonDao().deleteLesson(lesson)
                    dialog.dismiss()
                }

                dialogView.yoq.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setView(dialogView.root)
                dialog.show()
            }

            override fun onItemEditClick(lesson: Lesson) {
                var bundle = Bundle()
                bundle.putSerializable("edit",lesson)
                findNavController().navigate(R.id.lessonEditFragment,bundle)
            }

        })

        appDatabase.lessonDao().getLessonByModuleId(modul.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Consumer<List<Lesson>> {
                override fun accept(t: List<Lesson>?) {
                    lessonAdapter.submitList(t)
                }

            }, object : Consumer<Throwable> {
                override fun accept(t: Throwable?) {

                }

            })
        binding.rvlesson.adapter = lessonAdapter
    }

    private fun addLesson() {
        val modul = arguments?.getSerializable("modar") as Modul
        binding.kurs.text = modul.mod_name

        val lesson = Lesson()

        binding.floatingActionButton.setOnClickListener {
            lesson.lesson_name = binding.lessonName.text.toString().trim()
            lesson.lesson_description = binding.lessonInfo.text.toString().trim()
            lesson.lesson_position = binding.lessonPosition.text.toString().trim()
            lesson.lesson_image = modul.mod_image
            lesson.lesson_modul_id = modul.id
            Observable.fromCallable {
                appDatabase.lessonDao().addLesson(lesson)
            }.subscribe{
                Toast.makeText(binding.root.context, "added", Toast.LENGTH_SHORT).show()
                binding.lessonName.setText("")
                binding.lessonInfo.setText("")
                binding.lessonPosition.setText("")

            }

        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LessonFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LessonFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}