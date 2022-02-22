package com.example.pdp_online.kurs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdp_online.R
import com.example.pdp_online.database.AppDatabase
import com.example.pdp_online.databinding.FragmentEditModuleBinding
import com.example.pdp_online.databinding.FragmentLessonEditBinding
import com.example.pdp_online.entity.Lesson
import com.example.pdp_online.entity.Modul

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LessonEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LessonEditFragment : Fragment() {
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

    lateinit var binding: FragmentLessonEditBinding
    lateinit var appDatabase: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLessonEditBinding.inflate(inflater,container,false)
        appDatabase = AppDatabase.getInstance(binding.root.context)


        setElelments()
        updateLesson()

        return binding.root
    }

    private fun updateLesson() {
        val lesson = arguments?.getSerializable("edit") as Lesson
        binding.floatingActionButton.setOnClickListener {
            lesson.lesson_name = binding.lessonName.text.toString().trim()
            lesson.lesson_description = binding.lessonInfo.text.toString().trim()
            lesson.lesson_position = binding.lessonPosition.text.toString().trim()
            appDatabase.lessonDao().updateLesson(lesson)
            findNavController().popBackStack()
        }
    }

    private fun setElelments() {
        val lesson = arguments?.getSerializable("edit") as Lesson
        binding.kurs.text = "${lesson.lesson_position}-dars"
        binding.lessonName.setText(lesson.lesson_name)
        binding.lessonInfo.setText(lesson.lesson_description)
        binding.lessonPosition.setText(lesson.lesson_position)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LessonEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LessonEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}