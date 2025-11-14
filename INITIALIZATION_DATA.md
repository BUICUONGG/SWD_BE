# 📊 INITIALIZATION DATA SUMMARY

## 🔧 Configuration

- **MAX_TEAM_SIZE**: 6 members per team
- **TOTAL_STUDENTS**: 30 students
- **TOTAL_TEACHERS (MENTORS)**: 3 teachers
- **TOTAL_TEAMS**: 7 teams

---

## 👤 Default Accounts

### 1️⃣ ADMIN (1 account)
| Email | Password | Role | Full Name |
|-------|----------|------|-----------|
| admin@fpt.edu.vn | admin123 | ADMIN | System Admin |

### 2️⃣ TEACHERS/MENTORS (3 accounts)
| Email | Password | Role | Full Name | Short Name |
|-------|----------|------|-----------|------------|
| mentor1@fpt.edu.vn | mentor123 | MENTOR | Nguyen Van Mentor | MentorNV |
| mentor2@fpt.edu.vn | mentor123 | MENTOR | Tran Thi Huong | HuongTT |
| mentor3@fpt.edu.vn | mentor123 | MENTOR | Le Van Tuan | TuanLV |

### 3️⃣ STUDENTS (30 accounts)
| Email Pattern | Password | Role | Major |
|---------------|----------|------|-------|
| student1@fpt.edu.vn | student123 | STUDENT | Software Engineering |
| student2@fpt.edu.vn | student123 | STUDENT | Software Engineering |
| ... | ... | ... | ... |
| student30@fpt.edu.vn | student123 | STUDENT | Software Engineering |

**Full Student Names** (randomly generated):
- Nguyen Van An, Tran Van Binh, Le Van Cuong, Pham Van Dung, etc.
- Mix of Male and Female (50/50)
- Date of Birth: 2000-2004

---

## 📚 Course & Academic Data

### Subject
- **Code**: EXE201
- **Name**: Experiential Entrepreneurship 2
- **Prerequisites**: EXE101

### Semester
- **Code**: FA2025
- **Name**: Fall 2025
- **Year**: 2025
- **Term**: FALL
- **Start Date**: September 1, 2025
- **End Date**: December 31, 2025

### Course
- **Code**: EXE201_FA25_01
- **Name**: EXE201 - Fall 2025 - Class 01
- **Subject**: EXE201
- **Semester**: FA2025
- **Teacher/Mentor**: Nguyen Van Mentor (mentor1@fpt.edu.vn)
- **Status**: OPEN
- **Max Students**: 50
- **Enrolled Students**: 30

### Major
- **Code**: SE
- **Name**: Software Engineering
- **Status**: Active

---

## 👥 TEAMS (7 teams with max 6 members each)

### Team Distribution

| # | Team Name | Members | Leader | Ideas |
|---|-----------|---------|--------|-------|
| 1 | Alpha Team | 6 | student1 | 12 ideas |
| 2 | Beta Squad | 6 | student7 | 12 ideas |
| 3 | Gamma Force | 6 | student13 | 12 ideas |
| 4 | Delta Warriors | 6 | student19 | 12 ideas |
| 5 | Epsilon Innovators | 3 | student25 | 6 ideas |
| 6 | Zeta Creators | 2 | student28 | 4 ideas |
| 7 | Eta Pioneers | 1 | student30 | 2 ideas |

**Total**: 30 students distributed across 7 teams

### Team Details

#### 1. Alpha Team (6 members)
- **Leader**: student1@fpt.edu.vn
- **Members**: student1, student2, student3, student4, student5, student6
- **Main Idea**: First idea from student1

#### 2. Beta Squad (6 members)
- **Leader**: student7@fpt.edu.vn
- **Members**: student7, student8, student9, student10, student11, student12
- **Main Idea**: First idea from student7

#### 3. Gamma Force (6 members)
- **Leader**: student13@fpt.edu.vn
- **Members**: student13, student14, student15, student16, student17, student18
- **Main Idea**: First idea from student13

#### 4. Delta Warriors (6 members)
- **Leader**: student19@fpt.edu.vn
- **Members**: student19, student20, student21, student22, student23, student24
- **Main Idea**: First idea from student19

#### 5. Epsilon Innovators (3 members)
- **Leader**: student25@fpt.edu.vn
- **Members**: student25, student26, student27
- **Main Idea**: First idea from student25

#### 6. Zeta Creators (2 members)
- **Leader**: student28@fpt.edu.vn
- **Members**: student28, student29
- **Main Idea**: First idea from student28

#### 7. Eta Pioneers (1 member)
- **Leader**: student30@fpt.edu.vn
- **Members**: student30
- **Main Idea**: First idea from student30

---

## 💡 IDEAS

### Ideas per Student
- Each student/team member creates **2 ideas**
- **Format**: "Idea 1 - [Student Full Name]", "Idea 2 - [Student Full Name]"
- **Description**: "Great startup idea for team [Team Name]"

### Total Ideas
- **Total**: ~60 ideas (30 students × 2 ideas each)
- Each team's **first idea from the leader** becomes the team's **main idea**

---

## 📈 Summary Statistics

| Entity | Count |
|--------|-------|
| **Users** | 34 total |
| - Admin | 1 |
| - Teachers (Mentors) | 3 |
| - Students | 30 |
| **Majors** | 1 (SE) |
| **Subjects** | 1 (EXE201) |
| **Semesters** | 1 (FA2025) |
| **Courses** | 1 |
| **Enrollments** | 30 |
| **Teams** | 7 |
| **Team Members** | 30 |
| **Ideas** | ~60 |

---

## 🔑 Quick Test Accounts

### For Testing as ADMIN:
```
Email: admin@fpt.edu.vn
Password: admin123
```

### For Testing as TEACHER/MENTOR:
```
Email: mentor1@fpt.edu.vn
Password: mentor123
```

### For Testing as STUDENT (Team Leader):
```
Email: student1@fpt.edu.vn
Password: student123
(Leader of Alpha Team)
```

### For Testing as STUDENT (Team Member):
```
Email: student2@fpt.edu.vn
Password: student123
(Member of Alpha Team)
```

### For Testing as STUDENT (Solo Team):
```
Email: student30@fpt.edu.vn
Password: student123
(Solo member of Eta Pioneers)
```

---

## 🚀 How to Run

1. Make sure database is configured in `.env` or `application-dev.yml`
2. Run the application:
   ```bash
   mvnw spring-boot:run
   ```
3. On first run, all data will be automatically initialized
4. Check logs for initialization progress

---

## 📝 Notes

- **Idempotent**: Running multiple times won't duplicate data (uses `orElseGet()`)
- **Team Leaders**: First member added to each team automatically becomes the leader
- **Main Ideas**: Each team's main idea is automatically set to the leader's first idea
- **Password Encoding**: All passwords are bcrypt encoded
- **Timestamps**: All entities have automatic `createdAt` and `updatedAt` fields

---

**Last Updated**: November 14, 2025
**Version**: 1.0.0

