# 🔐 TÀI KHOẢN TEST - 3 LUỒNG

## 📋 TỔNG QUAN

Hệ thống có 3 role chính với các quyền khác nhau:
- **ADMIN**: Quản trị viên - Toàn quyền quản lý hệ thống
- **MENTOR (TEACHER)**: Giáo viên - Tạo course, quản lý sinh viên
- **STUDENT**: Sinh viên - Tham gia course, tạo team, tạo idea

---

## 1️⃣ TÀI KHOẢN ADMIN (Quản trị viên)

### Thông tin đăng nhập:
```
Email: admin@fpt.edu.vn
Password: admin123
```

### Thông tin chi tiết:
- **Full Name**: System Admin
- **Role**: ADMIN
- **Date of Birth**: January 1, 1990
- **Gender**: Male

### Quyền hạn:
✅ **Quản lý người dùng (Users)**
- Tạo user mới (admin, mentor, student)
- Xem danh sách tất cả users
- Tìm kiếm users
- Xóa/khôi phục users
- Import sinh viên từ Excel

✅ **Quản lý học thuật**
- Tạo/sửa/xóa Courses
- Tạo/sửa/xóa Majors (Chuyên ngành)
- Tạo/sửa/xóa Subjects (Môn học)
- Tạo/sửa/xóa Semesters (Học kỳ)
- Tạo/sửa/xóa Mentor Profiles
- Xóa Enrollments

✅ **Xem tất cả dữ liệu**
- Xem tất cả courses, enrollments, teams
- Xem chi tiết students, mentors
- Truy cập full system

### API Endpoints (Admin only):
```
POST   /api/users                          - Tạo user mới
GET    /api/users                          - Lấy danh sách users
GET    /api/users/{id}                     - Xem user theo ID
GET    /api/users/search?keyword=          - Tìm kiếm users
DELETE /api/users/{id}                     - Xóa user
PUT    /api/users/{id}/restore             - Khôi phục user
POST   /api/users/import                   - Import sinh viên từ Excel

POST   /api/majors                         - Tạo major
PUT    /api/majors/{id}                    - Sửa major
DELETE /api/majors/{id}                    - Xóa major

POST   /api/subjects                       - Tạo subject
PUT    /api/subjects/{id}                  - Sửa subject
DELETE /api/subjects/{id}                  - Xóa subject

POST   /api/semesters                      - Tạo semester
PUT    /api/semesters/{id}                 - Sửa semester
DELETE /api/semesters/{id}                 - Xóa semester

DELETE /api/courses/{id}                   - Xóa course
DELETE /api/enrollments/{id}               - Xóa enrollment

POST   /api/mentor-profiles                - Tạo mentor profile
PUT    /api/mentor-profiles/{id}           - Sửa mentor profile
DELETE /api/mentor-profiles/{id}           - Xóa mentor profile
```

---

## 2️⃣ TÀI KHOẢN TEACHER/MENTOR (Giáo viên)

### Thông tin đăng nhập:
```
Email: mentor1@fpt.edu.vn
Password: mentor123
```

### Thông tin chi tiết:
- **Full Name**: Nguyen Van Mentor
- **Short Name**: MentorNV
- **Role**: MENTOR
- **Date of Birth**: March 15, 1985
- **Gender**: Male

### Thông tin course:
- Đang dạy course: **EXE201 - Fall 2025 - Class 01**
- Course Code: **EXE201_FA25_01**
- Có **30 students** đã enroll
- Có **7 teams** đã được tạo

### Quyền hạn:
✅ **Quản lý Courses**
- Tạo course mới
- Cập nhật course của mình
- Xem danh sách courses

✅ **Quản lý Enrollments**
- Xem danh sách sinh viên đã enroll vào course
- Xem enrollment theo user
- Tìm kiếm enrollments

✅ **Xem thông tin Students**
- Xem thông tin cá nhân của mình
- Xem danh sách teams trong course
- Xem danh sách enrollments

✅ **Xem dữ liệu public**
- Xem courses, semesters, subjects, majors
- Xem mentor profiles

### API Endpoints (Mentor có thể dùng):
```
POST   /api/courses                        - Tạo course mới
PUT    /api/courses/{id}                   - Cập nhật course
GET    /api/courses                        - Xem courses
GET    /api/courses/{id}                   - Xem chi tiết course
GET    /api/courses/search                 - Tìm kiếm courses
GET    /api/courses/mentor/{mentorId}      - Xem courses của mentor

GET    /api/enrollments/{id}               - Xem enrollment
GET    /api/enrollments/user/{userId}      - Xem enrollments của user
GET    /api/enrollments/course/{courseId}  - Xem enrollments của course
GET    /api/enrollments/search             - Tìm kiếm enrollments

GET    /api/users/me                       - Xem thông tin cá nhân
GET    /api/teams?CourseId=&mentorId=      - Xem teams trong course

GET    /api/majors, /api/subjects, /api/semesters  - Xem dữ liệu public
```

### Test Scenarios cho Teacher:
1. **Xem danh sách sinh viên trong course**
   ```
   GET /api/enrollments/course/1
   ```

2. **Xem danh sách teams trong course**
   ```
   GET /api/teams?CourseId=1&mentorId=2
   ```

3. **Tạo course mới**
   ```
   POST /api/courses
   Body: {
     "code": "EXE201_FA25_02",
     "name": "EXE201 - Fall 2025 - Class 02",
     ...
   }
   ```

---

## 3️⃣ TÀI KHOẢN STUDENT (Sinh viên)

### 🎯 STUDENT 1 - Team Leader (Khuyến nghị test)
```
Email: student1@fpt.edu.vn
Password: student123
```

**Thông tin:**
- **Full Name**: Nguyen Van An
- **Role**: STUDENT
- **Major**: Software Engineering
- **Team**: Alpha Team (LEADER - 6 members)
- **Enrollment**: Đã enroll vào course EXE201_FA25_01
- **Ideas**: Có 2 ideas đã tạo

**Đặc biệt**: 
- ✅ Là **LEADER** của Alpha Team
- ✅ Có thể test đầy đủ tính năng leader:
  - Accept/Reject applications
  - Invite members
  - Select main idea
  - Kick members
  - Update team name
  - Disband team

---

### 👤 STUDENT 2 - Team Member
```
Email: student2@fpt.edu.vn
Password: student123
```

**Thông tin:**
- **Full Name**: Tran Van Binh
- **Role**: STUDENT
- **Major**: Software Engineering
- **Team**: Alpha Team (MEMBER - không phải leader)
- **Enrollment**: Đã enroll vào course EXE201_FA25_01
- **Ideas**: Có 2 ideas đã tạo

**Đặc biệt**:
- ✅ Là **MEMBER** của Alpha Team
- ✅ Có thể test tính năng member:
  - Leave team
  - Create ideas
  - View team info

---

### 🆕 STUDENT 30 - Solo/No Team
```
Email: student30@fpt.edu.vn
Password: student123
```

**Thông tin:**
- **Full Name**: Ngo Van Linh
- **Role**: STUDENT
- **Major**: Software Engineering
- **Team**: Eta Pioneers (LEADER - 1 member duy nhất)
- **Enrollment**: Đã enroll vào course EXE201_FA25_01
- **Ideas**: Có 2 ideas đã tạo

**Đặc biệt**:
- ✅ Team chỉ có 1 người (solo team)
- ✅ Có thể test:
  - Invite người khác vào team
  - Create new team (nếu muốn)

---

## 🎯 QUYỀN HẠN STUDENT

### ✅ Quản lý Enrollments:
```
POST   /api/enrollments                    - Đăng ký vào course
DELETE /api/enrollments/my-courses/{id}    - Hủy đăng ký
```

### ✅ Quản lý Teams:
```
POST   /api/teams/create                   - Tạo team mới
GET    /api/teams/my-teams?enrollmentId=   - Xem team của mình
GET    /api/teams/{teamId}                 - Xem chi tiết team
PUT    /api/teams/{teamId}                 - Cập nhật tên team (leader only)
PUT    /api/teams/{teamId}/select-idea     - Chọn idea chính (leader only)
DELETE /api/teams/{teamId}/members/{id}    - Kick member (leader only)
POST   /api/teams/{teamId}/leave           - Rời khỏi team (member only)
DELETE /api/teams/{teamId}                 - Giải tán team (leader only)
```

### ✅ Quản lý Ideas:
```
POST   /api/ideas/create                   - Tạo idea mới
GET    /api/ideas/my-ideas?enrollmentId=   - Xem ideas của mình
GET    /api/ideas/{ideaId}                 - Xem chi tiết idea
GET    /api/ideas/team/{teamId}            - Xem ideas của team
PUT    /api/ideas/{ideaId}                 - Sửa idea (owner only)
DELETE /api/ideas/{ideaId}                 - Xóa idea (owner only)
```

### ✅ Quản lý Applications:
```
POST   /api/applications/apply             - Gửi đơn join team
POST   /api/applications/invite            - Mời người khác (leader only)
PUT    /api/applications/{id}/handle       - Xử lý đơn (leader only)
GET    /api/applications/my-applications   - Xem đơn của mình
GET    /api/applications/team/{teamId}     - Xem đơn của team (leader only)
DELETE /api/applications/{id}              - Hủy đơn
```

### ✅ Xem dữ liệu public:
```
GET    /api/users/me                       - Xem thông tin cá nhân
GET    /api/courses, /api/majors, /api/subjects, /api/semesters
```

---

## 📝 TEST SCENARIOS CHI TIẾT

### Scenario 1: Student tạo team mới
**Account**: student30@fpt.edu.vn (hoặc bất kỳ student nào chưa có team)

1. Login với student30@fpt.edu.vn
2. Lấy enrollmentId từ `/api/enrollments/user/{userId}`
3. Tạo team mới:
   ```
   POST /api/teams/create?enrollmentId={id}&teamName=New Team
   ```

---

### Scenario 2: Student apply vào team khác
**Account**: Cần 1 student chưa có team

1. Login với student account
2. Xem danh sách teams: `GET /api/teams?CourseId=1&mentorId=2`
3. Apply vào team:
   ```
   POST /api/applications/apply?enrollmentId={id}&teamId={teamId}
   ```

---

### Scenario 3: Leader xử lý đơn
**Account**: student1@fpt.edu.vn (Leader của Alpha Team)

1. Login với student1@fpt.edu.vn
2. Xem đơn của team:
   ```
   GET /api/applications/team/{teamId}?leaderEnrollmentId={id}
   ```
3. Accept/Reject đơn:
   ```
   PUT /api/applications/{applicationId}/handle?leaderEnrollmentId={id}&accepted=true
   ```

---

### Scenario 4: Leader mời member vào team
**Account**: student1@fpt.edu.vn (Leader)

1. Login với student1@fpt.edu.vn
2. Lấy danh sách students trong course
3. Invite student khác:
   ```
   POST /api/applications/invite?leaderEnrollmentId={leaderId}&targetEnrollmentId={targetId}
   ```

---

### Scenario 5: Leader chọn main idea
**Account**: student1@fpt.edu.vn (Leader)

1. Login với student1@fpt.edu.vn
2. Xem ideas của team: `GET /api/ideas/team/{teamId}`
3. Chọn idea chính:
   ```
   PUT /api/teams/{teamId}/select-idea?leaderEnrollmentId={id}&ideaId={ideaId}
   ```

---

### Scenario 6: Member rời team
**Account**: student2@fpt.edu.vn (Member)

1. Login với student2@fpt.edu.vn
2. Rời khỏi team:
   ```
   POST /api/teams/{teamId}/leave?enrollmentId={id}
   ```

---

### Scenario 7: Leader kick member
**Account**: student1@fpt.edu.vn (Leader)

1. Login với student1@fpt.edu.vn
2. Kick member:
   ```
   DELETE /api/teams/{teamId}/members/{enrollmentId}?leaderEnrollmentId={leaderId}
   ```

---

### Scenario 8: Teacher xem danh sách students và teams
**Account**: mentor1@fpt.edu.vn

1. Login với mentor1@fpt.edu.vn
2. Xem enrollments: `GET /api/enrollments/course/1`
3. Xem teams: `GET /api/teams?CourseId=1&mentorId=2`

---

### Scenario 9: Admin quản lý users
**Account**: admin@fpt.edu.vn

1. Login với admin@fpt.edu.vn
2. Xem tất cả users: `GET /api/users`
3. Tạo user mới: `POST /api/users`
4. Xóa user: `DELETE /api/users/{id}`
5. Import Excel: `POST /api/users/import`

---

## 🔗 THÔNG TIN BỔ SUNG

### Database IDs (ước tính sau khi init):
- **Admin User ID**: 1
- **Mentor 1 User ID**: 2
- **Student 1 User ID**: 4
- **Student 2 User ID**: 5
- **Student 30 User ID**: 33
- **Course ID**: 1
- **Alpha Team ID**: 1
- **Enrollment của Student 1**: 1
- **Enrollment của Student 2**: 2

### Teams trong hệ thống:
1. **Alpha Team** - 6 members (student1-6, leader: student1)
2. **Beta Squad** - 6 members (student7-12, leader: student7)
3. **Gamma Force** - 6 members (student13-18, leader: student13)
4. **Delta Warriors** - 6 members (student19-24, leader: student19)
5. **Epsilon Innovators** - 3 members (student25-27, leader: student25)
6. **Zeta Creators** - 2 members (student28-29, leader: student28)
7. **Eta Pioneers** - 1 member (student30, leader: student30)

---

## ⚠️ LƯU Ý QUAN TRỌNG

### 1. Authorization Headers:
Tất cả API (trừ login/register) đều cần JWT token trong header:
```
Authorization: Bearer {your_jwt_token}
```

### 2. Lấy JWT Token:
```
POST /api/auth/login
Body: {
  "email": "admin@fpt.edu.vn",
  "password": "admin123"
}

Response: {
  "data": {
    "token": "eyJhbGciOiJIUzI1...",
    "refreshToken": "..."
  }
}
```

### 3. Swagger UI:
Truy cập Swagger để test API dễ dàng:
```
http://localhost:8080/swagger-ui.html
```

### 4. API Base URL:
```
http://localhost:8080/api
```

---

## 📊 TỔNG KẾT

| Role | Email | Password | Mục đích test |
|------|-------|----------|---------------|
| **ADMIN** | admin@fpt.edu.vn | admin123 | Test quản lý toàn hệ thống |
| **MENTOR** | mentor1@fpt.edu.vn | mentor123 | Test tính năng giáo viên |
| **STUDENT (Leader)** | student1@fpt.edu.vn | student123 | Test tính năng leader (đầy đủ nhất) |
| **STUDENT (Member)** | student2@fpt.edu.vn | student123 | Test tính năng member |
| **STUDENT (Solo)** | student30@fpt.edu.vn | student123 | Test invite, create team |

---

**Ngày tạo**: November 14, 2025  
**Version**: 1.0.0  
**Status**: ✅ Ready to test

🎯 **Khuyến nghị**: Bắt đầu test với **student1@fpt.edu.vn** vì tài khoản này là **Team Leader** và có thể test đầy đủ nhất các tính năng!

