package com.cmpt276a2.a2v2.controllers;

import com.cmpt276a2.a2v2.models.*;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class studentController {
    
    @Autowired
    private StudentRepository studentRepo;

    // default 
    @GetMapping("students/view")
    public String getAllStudents(Model model) {
        List<Student> students = studentRepo.findAll();
        model.addAttribute("students", students);
        model.addAttribute("edit", new Student());
        return "students/display";
    }

    // add a student
    @PostMapping("students/view")
    public String addStudent(@RequestParam Map<String, String> newStudent, HttpServletResponse response, Model model) {
        String newName = newStudent.get("nameTextBox");
        int newHeight = Integer.parseInt(newStudent.get("heightTextBox"));
        int newWeight = Integer.parseInt(newStudent.get("weightTextBox"));
        String newHairColor = newStudent.get("hairTextBox");
        float newGpa = Float.parseFloat(newStudent.get("gpaTextBox"));
        String newFavFood = newStudent.get("foodTextBox");

        studentRepo.save(new Student(newName, newHeight, newWeight, newHairColor, newGpa, newFavFood));
        response.setStatus(201);

        List<Student> students = studentRepo.findAll();
        model.addAttribute("students", students);

        return "redirect:/students/view";
    }

    // delete student
    @PostMapping("students/delete")
    public String deleteStudent(@RequestParam Map<String, String> id, HttpServletResponse response, Model model) {
        System.out.println("delete user: " + id.get("studentId"));

        studentRepo.deleteById(Integer.parseInt(id.get("studentId")));

        List<Student> students = studentRepo.findAll();
        model.addAttribute("students", students);
        return "redirect:/students/view";
    }

    // curent student being edited
    private Student editedStudent;


    // edit student
    @PostMapping("students/edit")
    public String editStudent(@RequestParam Map<String, String> id, HttpServletResponse response, Model model) {
        System.out.println("edit user: " + id.get("studentId"));
        Student student = studentRepo.getReferenceById(Integer.parseInt(id.get("studentId")));

        model.addAttribute("edit", student);
        editedStudent = student;
        return "students/editStudent";
    }

    // update student after edited
    @PostMapping("students/editStudent")
    public String editStudentReturn(@RequestParam Map<String, String> editStudent, HttpServletResponse response, Model model) {
        editedStudent.setName(editStudent.get("nameTextBox"));
        editedStudent.setHeight(Integer.parseInt(editStudent.get("heightTextBox")));
        editedStudent.setWeight(Integer.parseInt(editStudent.get("weightTextBox")));
        editedStudent.setHair(editStudent.get("hairTextBox"));
        editedStudent.setGpa(Float.parseFloat(editStudent.get("gpaTextBox")));
        editedStudent.setFavFood(editStudent.get("foodTextBox"));
        studentRepo.save(editedStudent);

        List<Student> students = studentRepo.findAll();
        model.addAttribute("students", students);
        return "redirect:/students/view";
    }
}