package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "管理端-员工相关的接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation(value = "员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        //若登录失败会出示错误信息并抛出异常，由全局异常处理器捕获
        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());//打包当前登录用户的id
        String token = JwtUtil.createJWT(//用JwtUtil工具类生成令牌，加密为token
                jwtProperties.getAdminSecretKey(),//密钥
                jwtProperties.getAdminTtl(),//有效期
                claims);//声明信息

        //将用户信息和令牌封装到VO中返回给前端
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工注销")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 新增员工
     *
 * @param employeeDTO
     * @return com.sky.result.Result
     * @author DuRuiChi
     * @create 2024/10/25
     **/
    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        employeeService.saveEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
 * @param employeePageQueryDTO
     * @return com.sky.result.Result<com.sky.result.PageResult>
     * @author DuRuiChi
     * @create 2024/10/25
     **/
    @GetMapping("page")
    @ApiOperation(value = "员工分页查询")
    public Result<PageResult> getEmployeePage(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}", employeePageQueryDTO);
        PageResult page =employeeService.getEmployeePage(employeePageQueryDTO);
        return Result.success(page);
    }

    /**
     * 启用禁用员工账号
     *
 * @param status
 * @param id
     * @return com.sky.result.Result
     * @author DuRuiChi
     * @create 2024/10/25
     **/
    @PostMapping("status/{status}")
    @ApiOperation(value = "启用禁用员工账号")
    public Result startOrStopStatus(@PathVariable Integer status,Long id){
        log.info("启用或禁用员工账号：{}，{}",status,id);
        employeeService.startOrStopStatus(status,id);
        return Result.success();
    }

    /**
     * 根据id查询员工
     *
 * @param id
     * @return com.sky.result.Result<com.sky.entity.Employee>
     * @author DuRuiChi
     * @create 2024/10/25
     **/
    @GetMapping("{id}")
    @ApiOperation(value = "根据id查询员工")
    public Result<Employee> getEmployeeById(@PathVariable Long id) {
        log.info("根据id查询员工：{}", id);
        Employee emp = employeeService.getEmployeeById(id);
        return Result.success(emp);
    }

    /**
     * 修改员工信息
     *
 * @param employeeDTO
     * @return com.sky.result.Result
     * @author DuRuiChi
     * @create 2024/10/25
     **/
    @PutMapping
    @ApiOperation(value = "修改员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工信息：{}", employeeDTO);
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }
}
