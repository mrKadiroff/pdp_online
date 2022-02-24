package com.example.pdp_online.kurs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdp_online.R
import com.example.pdp_online.databinding.FragmentLessonChildBinding
import com.example.pdp_online.databinding.FragmentSettingsBinding
import com.example.pdp_online.entity.Lesson
import com.example.pdp_online.entity.Modul

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LessonChildFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LessonChildFragment : Fragment() {
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
    lateinit var binding: FragmentLessonChildBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLessonChildBinding.inflate(inflater,container,false)

        setUI()

        return binding.root
    }

    private fun setUI() {
        val dars= arguments?.getSerializable("dars") as Lesson

        binding.lessonName.text = dars.lesson_name
        binding.lessonLocation.text = "${dars.lesson_position}-dars"
        binding.lessonInfo.text = dars.lesson_description
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LessonChildFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LessonChildFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}