package top.cs.boot.mp.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.cs.boot.mp.entity.UserAccount;
import top.cs.boot.mp.mapper.UserAccountMapper;

import java.util.List;

@RestController
@RequestMapping("/user-accounts")
public class UserAccountController {
    @Resource
    private UserAccountMapper userAccountMapper;

    @GetMapping("/{id}")
    public UserAccount getUserAccountById(@PathVariable Long id) {
        return userAccountMapper.selectById(id);
    }

    // 查询所有
    @GetMapping("/list")
    public List<UserAccount> list() {
        return userAccountMapper.selectList(null);
    }
    // 新增
    @PostMapping("/add")
    public String add(@RequestBody UserAccount userAccount){
        userAccountMapper.insert(userAccount);
        return "添加成功";
    }
    // 修改
    @PostMapping("/update")
    public String update(@RequestBody UserAccount userAccount){
        userAccountMapper.updateById(userAccount);
        return "修改成功";
    }
    // 删除
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        userAccountMapper.deleteById(id);
        return "删除成功";
    }
}
