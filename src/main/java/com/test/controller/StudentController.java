package com.test.controller;

import com.test.model.Student;
import com.test.model.StudentForm;
import com.test.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    @Value("${file_upload}")
    private String fileUpload;

    @GetMapping("")
    public String index(Model model) {
        List<Student> studentList = studentService.findAll();
        model.addAttribute("students", studentList);
        return "/home";
    }
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("student", new StudentForm());
        return "/create";
    }

    @PostMapping("/save")
    public String save(StudentForm studentForm, RedirectAttributes redirectAttributes) {
        MultipartFile file = studentForm.getImageFile();
        String filename= file.getOriginalFilename();
        try {
            FileCopyUtils.copy(file.getBytes(), new File(fileUpload + filename));
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Student student = new Student(studentForm.getName(),studentForm.getAddress(),filename);
        studentService.save(student);
        redirectAttributes.addFlashAttribute("message","Successfully added student");
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable Long id, Model model) {
        Student studentOld = studentService.findById(id);
        StudentForm student = new StudentForm(studentOld.getId(),studentOld.getName(),studentOld.getAddress(),studentOld.getImage());
        model.addAttribute("student",student);
        return "/update";
    }

    @PostMapping("/update")
    public String update(StudentForm studentForm, RedirectAttributes redirectAttributes) {
        Student student = new Student(studentForm.getId(), studentForm.getName(), studentForm.getAddress(), studentForm.getImage());
        MultipartFile file = studentForm.getImageFile();
        if(!file.isEmpty()){
            String filename= file.getOriginalFilename();
            try {
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + filename));
                student.setImage(filename);
            }catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        studentService.save(student);
        redirectAttributes.addFlashAttribute("message","Successfully saved");
        return "redirect:/students";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes) {
        studentService.remove(id);
        redirectAttributes.addFlashAttribute("message","Successfully deleted");
        return "redirect:/students";
    }
    @GetMapping("/{id}/view")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "/view";
    }
    @GetMapping("/search")
    public String search(@RequestParam String q,Model model){
        List<Student> studentList = studentService.findByName(q);
        model.addAttribute("students", studentList);
        return "/home";
    }

}
