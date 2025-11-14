# 📋 PHÂN QUYỀN API - EXE GROUPING MANAGEMENT SYSTEM

## 🔑 ROLES TRONG HỆ THỐNG
- **ADMIN** - Quản trị viên hệ thống
- **MENTOR** - Giảng viên/Mentor
- **STUDENT** - Sinh viên

---

## 📱 1. USER MANAGEMENT (`/api/users`)

### ADMIN Only:
- ✅ `POST /api/users` - Tạo user mới
- ✅ `GET /api/users` - Lấy danh sách tất cả users
- ✅ `GET /api/users/{id}` - Lấy thông tin user theo ID
- ✅ `GET /api/users/search?keyword=` - Tìm kiếm user
- ✅ `DELETE /api/users/{id}` - Xóa user (soft delete)
- ✅ `PUT /api/users/{id}/restore` - Khôi phục user đã xóa
- ✅ `POST /api/users/import` - Import sinh viên từ Excel

### All Authenticated Users:
- ✅ `GET /api/users/me` - Lấy thông tin user hiện tại

---

## 🎓 2. COURSE MANAGEMENT (`/api/courses`)

### ADMIN + MENTOR:
- ✅ `POST /api/courses` - Tạo course mới
- ✅ `PUT /api/courses/{id}` - Cập nhật course

### ADMIN Only:
- ✅ `DELETE /api/courses/{id}` - Xóa course

### Public (permitAll):
- ✅ `GET /api/courses/{id}` - Lấy thông tin course theo ID
- ✅ `GET /api/courses/code/{code}` - Lấy thông tin course theo code
- ✅ `GET /api/courses` - Lấy danh sách tất cả courses
- ✅ `GET /api/courses/search` - Tìm kiếm courses (keyword, status, semester, mentor, subject)
- ✅ `GET /api/courses/status/{status}` - Lấy courses theo status
- ✅ `GET /api/courses/semester/{semesterId}` - Lấy courses theo semester
- ✅ `GET /api/courses/mentor/{mentorId}` - Lấy courses theo mentor

---

## 📝 3. ENROLLMENT MANAGEMENT (`/api/enrollments`)

### STUDENT Only:
- ✅ `POST /api/enrollments` - Đăng ký vào course
- ✅ `DELETE /api/enrollments/my-courses/{courseId}` - Hủy đăng ký course

### ADMIN + MENTOR:
- ✅ `GET /api/enrollments/{id}` - Lấy thông tin enrollment theo ID
- ✅ `GET /api/enrollments/user/{userId}` - Lấy enrollments theo user
- ✅ `GET /api/enrollments/course/{courseId}` - Lấy enrollments theo course
- ✅ `GET /api/enrollments/search` - Tìm kiếm enrollments

### ADMIN Only:
- ✅ `DELETE /api/enrollments/{id}` - Xóa enrollment

---

## 👥 4. TEAM MANAGEMENT (`/api/teams`)

### No Authorization Required (Authenticated):
- ✅ `POST /api/teams/create` - Tạo team mới (creator trở thành leader)
- ✅ `GET /api/teams/my-teams?enrollmentId=` - Xem team của mình
- ✅ `GET /api/teams?CourseId=&mentorId=` - Danh sách teams trong course
- ✅ `PUT /api/teams/{teamId}/select-idea?ideaId=` - Leader chọn idea chính

---

## 💡 5. IDEA MANAGEMENT (`/api/ideas`)

### No Authorization Required (Authenticated):
- ✅ `POST /api/ideas/create` - Student tạo idea mới
- ✅ `GET /api/ideas/my-ideas?enrollmentId=` - Xem list idea của mình

---

## 📨 6. APPLICATION MANAGEMENT (`/api/applications`)

### No Authorization Required (Authenticated):
- ✅ `POST /api/applications/apply` - Student gửi đơn join team
- ✅ `POST /api/applications/invite` - Leader mời student vào team
- ✅ `PUT /api/applications/{applicationId}/handle` - Leader xử lý đơn (accept/reject)

---

## 🏫 7. SEMESTER MANAGEMENT (`/api/semesters`)

### ADMIN Only:
- ✅ `POST /api/semesters` - Tạo semester mới
- ✅ `PUT /api/semesters/{id}` - Cập nhật semester
- ✅ `DELETE /api/semesters/{id}` - Xóa semester

### Public (permitAll):
- ✅ `GET /api/semesters/{id}` - Lấy thông tin semester theo ID
- ✅ `GET /api/semesters/code/{code}` - Lấy thông tin semester theo code
- ✅ `GET /api/semesters` - Lấy danh sách tất cả semesters

---

## 📚 8. SUBJECT MANAGEMENT (`/api/subjects`)

### ADMIN Only:
- ✅ `POST /api/subjects` - Tạo subject mới
- ✅ `PUT /api/subjects/{id}` - Cập nhật subject
- ✅ `DELETE /api/subjects/{id}` - Xóa subject

### Public (permitAll):
- ✅ `GET /api/subjects/{id}` - Lấy thông tin subject theo ID
- ✅ `GET /api/subjects/code/{code}` - Lấy thông tin subject theo code
- ✅ `GET /api/subjects` - Lấy danh sách tất cả subjects
- ✅ `GET /api/subjects/search?keyword=` - Tìm kiếm subjects

---

## 🎯 9. MAJOR MANAGEMENT (`/api/majors`)

### ADMIN Only:
- ✅ `POST /api/majors` - Tạo major mới
- ✅ `PUT /api/majors/{id}` - Cập nhật major
- ✅ `DELETE /api/majors/{id}` - Xóa major

### Public (permitAll):
- ✅ `GET /api/majors/{id}` - Lấy thông tin major theo ID
- ✅ `GET /api/majors/code/{code}` - Lấy thông tin major theo code
- ✅ `GET /api/majors` - Lấy danh sách tất cả majors
- ✅ `GET /api/majors/search?keyword=` - Tìm kiếm majors

---

## 👨‍🏫 10. MENTOR PROFILE MANAGEMENT (`/api/mentor-profiles`)

### ADMIN Only:
- ✅ `POST /api/mentor-profiles` - Tạo mentor profile mới
- ✅ `PUT /api/mentor-profiles/{id}` - Cập nhật mentor profile
- ✅ `DELETE /api/mentor-profiles/{id}` - Xóa mentor profile

### Public (permitAll):
- ✅ `GET /api/mentor-profiles/{id}` - Lấy thông tin mentor profile theo ID
- ✅ `GET /api/mentor-profiles/user/{userId}` - Lấy mentor profile theo user ID
- ✅ `GET /api/mentor-profiles` - Lấy danh sách tất cả mentor profiles
- ✅ `GET /api/mentor-profiles/search?keyword=` - Tìm kiếm mentor profiles

---

## 🔔 11. PUSH NOTIFICATION (`/api/notifications`)
**Chỉ hoạt động khi `firebase.enabled=true`**

### All Authenticated Users:
- ✅ `POST /api/notifications/register-token` - Đăng ký device token cho push notifications
- ✅ `DELETE /api/notifications/remove-token?token=` - Xóa device token (logout)
- ✅ `DELETE /api/notifications/remove-all-tokens` - Xóa tất cả device tokens
- ✅ `GET /api/notifications/my-devices` - Lấy danh sách devices đã đăng ký

---

## 📊 TỔNG KẾT PHÂN QUYỀN

### 🎓 STUDENT (Sinh viên):
**Quyền chính:**
- Đăng ký/Hủy đăng ký courses
- Tạo và quản lý teams
- Tạo và quản lý ideas
- Gửi đơn join team / Nhận lời mời vào team
- Xem thông tin các courses, semesters, subjects, majors, mentors (public)
- Quản lý push notifications của bản thân

**Các API chính:**
- ✅ Enrollment: Enroll, Unenroll
- ✅ Team: Create, View, Select idea
- ✅ Idea: Create, View
- ✅ Application: Apply, Invite, Handle
- ✅ User: Get my info
- ✅ All public GET APIs

---

### 👨‍🏫 MENTOR (Giảng viên):
**Quyền chính:**
- Tạo và cập nhật courses
- Xem danh sách enrollments trong courses của mình
- Xem thông tin students
- Quản lý push notifications
- Tất cả quyền public

**Các API chính:**
- ✅ Course: Create, Update, View all
- ✅ Enrollment: View by course, user, search
- ✅ User: Get my info
- ✅ All public GET APIs

---

### 👑 ADMIN (Quản trị viên):
**Quyền đầy đủ:**
- Quản lý users (CRUD, import Excel)
- Quản lý courses (Delete)
- Quản lý enrollments (Delete)
- Quản lý semesters (CRUD)
- Quản lý subjects (CRUD)
- Quản lý majors (CRUD)
- Quản lý mentor profiles (CRUD)
- Tất cả quyền của MENTOR và STUDENT

**Các API chính:**
- ✅ **Tất cả các API trong hệ thống**
- ✅ Đặc biệt: Import users từ Excel, Delete operations

---

## ⚠️ LƯU Ý QUAN TRỌNG

### Missing Authorization:
Một số controller **CHƯA CÓ** `@PreAuthorize`:
- ❌ `ApplicationController` - Tất cả APIs không có authorization
- ❌ `TeamController` - Tất cả APIs không có authorization  
- ❌ `IdeaController` - Tất cả APIs không có authorization

**Khuyến nghị:** Nên thêm authorization cho các controllers này:
```java
// ApplicationController
@PreAuthorize("hasAuthority('STUDENT')") // cho apply, invite
@PreAuthorize("isAuthenticated()") // cho handle

// TeamController  
@PreAuthorize("hasAuthority('STUDENT')") // cho create team
@PreAuthorize("isAuthenticated()") // cho các APIs khác

// IdeaController
@PreAuthorize("hasAuthority('STUDENT')") // cho create idea
@PreAuthorize("isAuthenticated()") // cho get my ideas
```

---

## 🔒 SECURITY CONFIGURATION
- JWT-based authentication
- Role-based authorization với Spring Security
- Redis cho refresh token management
- Firebase Cloud Messaging cho push notifications (optional)

---

**Ngày tạo:** November 14, 2025  
**Version:** 1.0.0

