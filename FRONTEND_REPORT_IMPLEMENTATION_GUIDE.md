# 📊 FRONTEND IMPLEMENTATION GUIDE - ADMIN DASHBOARD & REPORTS

## 🎯 OVERVIEW

Implement a comprehensive **Admin Dashboard** with detailed **Reports & Statistics** functionality for the EXE Grouping Management System.

---

## 📋 REQUIREMENTS

### Tech Stack Recommendation:
- **React/Next.js** with TypeScript
- **Chart Library**: Recharts or Chart.js
- **UI Components**: Ant Design, Material-UI, or Shadcn/ui
- **State Management**: React Query (for API calls) + Zustand/Redux
- **HTTP Client**: Axios

---

## 🔐 AUTHENTICATION

All API calls require JWT token in header:
```typescript
headers: {
  'Authorization': `Bearer ${accessToken}`
}
```

**Admin Login:**
```
POST /api/auth/login
Body: {
  "email": "admin@fpt.edu.vn",
  "password": "admin123"
}
```

---

## 📊 ADMIN DASHBOARD - MAIN FEATURES

### 1. **Overview Dashboard** (Landing Page)

**API Endpoint:**
```
GET /api/reports/dashboard
```

**Response Structure:**
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "totalUsers": 34,
    "totalStudents": 30,
    "totalMentors": 3,
    "totalCourses": 1,
    "totalEnrollments": 30,
    "totalTeams": 7,
    "totalIdeas": 60,
    "activeCourses": 1,
    "completedCourses": 0,
    "currentSemesterStats": {
      "semesterCode": "FA2025",
      "semesterName": "Fall 2025",
      "coursesCount": 1,
      "enrollmentsCount": 30,
      "teamsCount": 7,
      "averageTeamSize": 4.29
    }
  }
}
```

**UI Components to Display:**

#### A. **Statistics Cards** (Top Row)
Display 4 main cards in a grid:
```
┌─────────────────┬─────────────────┬─────────────────┬─────────────────┐
│   Total Users   │  Total Courses  │    Total Teams  │   Total Ideas   │
│      👥 34      │      📚 1       │      👥 7       │      💡 60      │
└─────────────────┴─────────────────┴─────────────────┴─────────────────┘
```

#### B. **User Distribution Chart** (Pie/Donut Chart)
```javascript
const userData = [
  { name: 'Students', value: data.totalStudents, color: '#3b82f6' },
  { name: 'Mentors', value: data.totalMentors, color: '#10b981' },
  { name: 'Admins', value: data.totalUsers - data.totalStudents - data.totalMentors, color: '#8b5cf6' }
];
```

#### C. **Course Status Chart** (Bar Chart)
```javascript
const courseData = [
  { status: 'Active', count: data.activeCourses },
  { status: 'Completed', count: data.completedCourses }
];
```

#### D. **Current Semester Overview** (Info Card)
```
┌─────────────────────────────────────────┐
│  📅 Fall 2025 (FA2025)                  │
│  • Courses: 1                           │
│  • Enrollments: 30                      │
│  • Teams: 7                             │
│  • Avg Team Size: 4.29                  │
└─────────────────────────────────────────┘
```

---

### 2. **Student Reports**

#### 2.1 **Report by Semester**

**API Endpoint:**
```
GET /api/reports/students/by-semester/{semesterId}
```

**Response:**
```json
{
  "data": {
    "reportType": "BY_SEMESTER",
    "filterName": "Fall 2025",
    "filterValue": 1,
    "totalStudents": 30,
    "enrolledStudents": 30,
    "studentsInTeams": 30,
    "studentsWithoutTeams": 0,
    "courseDetails": [
      {
        "courseId": 1,
        "courseCode": "EXE201_FA25_01",
        "courseName": "EXE201 - Fall 2025 - Class 01",
        "mentorName": "Nguyen Van Mentor",
        "enrolledCount": 30,
        "teamsCount": 7,
        "studentsInTeams": 30,
        "teamFormationRate": 100.0
      }
    ]
  }
}
```

**UI Components:**

1. **Semester Selector** (Dropdown)
   - Fetch semesters: `GET /api/semesters`
   - Display: "Fall 2025 (FA2025)"

2. **Summary Cards**
```
┌──────────────────┬──────────────────┬──────────────────┬──────────────────┐
│ Total Students   │ Enrolled         │ In Teams         │ Without Teams    │
│     30           │     30           │     30           │      0           │
└──────────────────┴──────────────────┴──────────────────┴──────────────────┘
```

3. **Course Details Table**
```
| Course Code      | Course Name              | Mentor           | Enrolled | Teams | In Teams | Rate   |
|------------------|--------------------------|------------------|----------|-------|----------|--------|
| EXE201_FA25_01   | EXE201 - Fall 2025 - 01 | Nguyen Van Mentor| 30       | 7     | 30       | 100%   |
```

4. **Team Formation Rate Chart** (Progress/Bar Chart)
   - X-axis: Course names
   - Y-axis: Team formation rate (%)

---

#### 2.2 **Report by Mentor/Teacher**

**API Endpoint:**
```
GET /api/reports/students/by-mentor/{mentorId}?semesterId={optional}
```

**UI Components:**

1. **Filters:**
   - Mentor Selector (Dropdown from `GET /api/users?role=MENTOR`)
   - Semester Filter (Optional)

2. **Display:** Same structure as "Report by Semester"

---

#### 2.3 **Report by Major**

**API Endpoint:**
```
GET /api/reports/students/by-major/{majorId}?semesterId={optional}
```

**UI Components:**

1. **Filters:**
   - Major Selector (Dropdown from `GET /api/majors`)
   - Semester Filter (Optional)

2. **Display:** Same structure as "Report by Semester"

---

### 3. **Mentor Performance Report**

**API Endpoint:**
```
GET /api/reports/mentors/performance?semesterId={optional}
```

**Response:**
```json
{
  "data": [
    {
      "mentorId": 2,
      "mentorName": "Nguyen Van Mentor",
      "shortName": "MentorNV",
      "totalCourses": 1,
      "totalStudents": 30,
      "totalTeams": 7,
      "averageStudentsPerCourse": 30.0,
      "averageTeamsPerCourse": 7.0,
      "teamFormationRate": 23.33,
      "courses": [
        {
          "courseId": 1,
          "courseCode": "EXE201_FA25_01",
          "courseName": "EXE201 - Fall 2025 - Class 01",
          "semesterCode": "FA2025",
          "studentsCount": 30,
          "teamsCount": 7,
          "ideasCount": 60
        }
      ]
    }
  ]
}
```

**UI Components:**

1. **Semester Filter** (Optional)

2. **Mentor Performance Cards**
   - Display each mentor as a card/accordion
   - Show key metrics: courses, students, teams, rates

3. **Comparison Chart**
   - Bar chart comparing all mentors
   - Metrics: Students count, Teams count, Team formation rate

4. **Detailed Table per Mentor**
```
| Mentor Name       | Courses | Students | Teams | Avg Students/Course | Team Formation Rate |
|-------------------|---------|----------|-------|---------------------|---------------------|
| Nguyen Van Mentor | 1       | 30       | 7     | 30.0                | 23.33%              |
| Tran Thi Huong    | 0       | 0        | 0     | 0.0                 | 0%                  |
```

---

### 4. **Additional Statistics Pages**

#### 4.1 **Course Statistics**

**API Endpoint:**
```
GET /api/reports/courses/statistics?semesterId={optional}
```

**Display:**
- Total courses (pie chart by status)
- Average students per course
- Timeline chart (courses by semester)

---

#### 4.2 **Team Statistics**

**API Endpoint:**
```
GET /api/reports/teams/statistics?semesterId={}&courseId={}
```

**Display:**
- Total teams
- Average team size
- Teams with/without main idea (pie chart)
- Team size distribution (histogram)

---

#### 4.3 **Enrollment Statistics**

**API Endpoint:**
```
GET /api/reports/enrollments/statistics?semesterId={}&majorId={}
```

**Display:**
- Total enrollments over time (line chart)
- Enrollments by major (bar chart)
- Top courses by enrollment count

---

## 🎨 UI/UX DESIGN GUIDELINES

### Layout Structure:

```
┌─────────────────────────────────────────────────────────────┐
│  Sidebar                    Main Content Area                │
├──────────────┬─────────────────────────────────────────────┤
│              │  📊 Dashboard Overview                        │
│ 📊 Dashboard │  ┌──────┬──────┬──────┬──────┐              │
│              │  │ Card │ Card │ Card │ Card │              │
│ 📈 Reports   │  └──────┴──────┴──────┴──────┘              │
│  ├ Students  │  ┌─────────────┬─────────────┐              │
│  ├ Mentors   │  │   Chart 1   │   Chart 2   │              │
│  ├ Courses   │  └─────────────┴─────────────┘              │
│  ├ Teams     │  ┌───────────────────────────┐              │
│  └ Enrolls   │  │   Detailed Table          │              │
│              │  └───────────────────────────┘              │
│ 👥 Users     │                                              │
│ 📚 Courses   │                                              │
│ 🎓 Semesters │                                              │
└──────────────┴─────────────────────────────────────────────┘
```

### Color Scheme:
- **Primary**: Blue (#3b82f6) - Main actions
- **Success**: Green (#10b981) - Positive metrics
- **Warning**: Yellow (#f59e0b) - Attention needed
- **Danger**: Red (#ef4444) - Critical issues
- **Info**: Purple (#8b5cf6) - Information

### Chart Types:
- **Statistics Cards**: Large numbers with icons
- **Pie/Donut Charts**: Distribution (users, teams)
- **Bar Charts**: Comparisons (mentors, courses)
- **Line Charts**: Trends over time
- **Progress Bars**: Percentages (team formation rate)
- **Tables**: Detailed data with sorting/filtering

---

## 💻 CODE EXAMPLES

### 1. **API Service (TypeScript)**

```typescript
// services/reportService.ts
import axios from 'axios';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const reportApi = {
  getDashboard: async () => {
    const response = await axios.get(`${API_BASE_URL}/reports/dashboard`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });
    return response.data.data;
  },

  getStudentsBySemester: async (semesterId: number) => {
    const response = await axios.get(
      `${API_BASE_URL}/reports/students/by-semester/${semesterId}`,
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        }
      }
    );
    return response.data.data;
  },

  getStudentsByMentor: async (mentorId: number, semesterId?: number) => {
    const url = semesterId 
      ? `${API_BASE_URL}/reports/students/by-mentor/${mentorId}?semesterId=${semesterId}`
      : `${API_BASE_URL}/reports/students/by-mentor/${mentorId}`;
    
    const response = await axios.get(url, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });
    return response.data.data;
  },

  getStudentsByMajor: async (majorId: number, semesterId?: number) => {
    const url = semesterId 
      ? `${API_BASE_URL}/reports/students/by-major/${majorId}?semesterId=${semesterId}`
      : `${API_BASE_URL}/reports/students/by-major/${majorId}`;
    
    const response = await axios.get(url, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });
    return response.data.data;
  },

  getMentorPerformance: async (semesterId?: number) => {
    const url = semesterId 
      ? `${API_BASE_URL}/reports/mentors/performance?semesterId=${semesterId}`
      : `${API_BASE_URL}/reports/mentors/performance`;
    
    const response = await axios.get(url, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });
    return response.data.data;
  },

  getCourseStatistics: async (semesterId?: number) => {
    const url = semesterId 
      ? `${API_BASE_URL}/reports/courses/statistics?semesterId=${semesterId}`
      : `${API_BASE_URL}/reports/courses/statistics`;
    
    const response = await axios.get(url, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });
    return response.data.data;
  },

  getTeamStatistics: async (semesterId?: number, courseId?: number) => {
    let url = `${API_BASE_URL}/reports/teams/statistics?`;
    if (semesterId) url += `semesterId=${semesterId}&`;
    if (courseId) url += `courseId=${courseId}`;
    
    const response = await axios.get(url, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });
    return response.data.data;
  },

  getEnrollmentStatistics: async (semesterId?: number, majorId?: number) => {
    let url = `${API_BASE_URL}/reports/enrollments/statistics?`;
    if (semesterId) url += `semesterId=${semesterId}&`;
    if (majorId) url += `majorId=${majorId}`;
    
    const response = await axios.get(url, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });
    return response.data.data;
  }
};

export default reportApi;
```

---

### 2. **Dashboard Component (React + TypeScript)**

```typescript
// components/AdminDashboard.tsx
import React, { useEffect, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import reportApi from '@/services/reportService';
import { Card, Row, Col, Statistic, Spin } from 'antd';
import { UserOutlined, BookOutlined, TeamOutlined, BulbOutlined } from '@ant-design/icons';
import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from 'recharts';

interface DashboardData {
  totalUsers: number;
  totalStudents: number;
  totalMentors: number;
  totalCourses: number;
  totalEnrollments: number;
  totalTeams: number;
  totalIdeas: number;
  activeCourses: number;
  completedCourses: number;
  currentSemesterStats: {
    semesterCode: string;
    semesterName: string;
    coursesCount: number;
    enrollmentsCount: number;
    teamsCount: number;
    averageTeamSize: number;
  };
}

const AdminDashboard: React.FC = () => {
  const { data, isLoading, error } = useQuery<DashboardData>({
    queryKey: ['dashboard'],
    queryFn: reportApi.getDashboard
  });

  if (isLoading) return <Spin size="large" />;
  if (error) return <div>Error loading dashboard</div>;
  if (!data) return null;

  const userDistribution = [
    { name: 'Students', value: data.totalStudents, color: '#3b82f6' },
    { name: 'Mentors', value: data.totalMentors, color: '#10b981' },
    { name: 'Admins', value: data.totalUsers - data.totalStudents - data.totalMentors, color: '#8b5cf6' }
  ];

  return (
    <div className="dashboard-container">
      <h1>Admin Dashboard</h1>

      {/* Statistics Cards */}
      <Row gutter={[16, 16]} style={{ marginBottom: '24px' }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Users"
              value={data.totalUsers}
              prefix={<UserOutlined />}
              valueStyle={{ color: '#3b82f6' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Courses"
              value={data.totalCourses}
              prefix={<BookOutlined />}
              valueStyle={{ color: '#10b981' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Teams"
              value={data.totalTeams}
              prefix={<TeamOutlined />}
              valueStyle={{ color: '#f59e0b' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Ideas"
              value={data.totalIdeas}
              prefix={<BulbOutlined />}
              valueStyle={{ color: '#8b5cf6' }}
            />
          </Card>
        </Col>
      </Row>

      {/* Charts Row */}
      <Row gutter={[16, 16]}>
        <Col xs={24} lg={12}>
          <Card title="User Distribution">
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={userDistribution}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {userDistribution.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </Card>
        </Col>

        <Col xs={24} lg={12}>
          <Card title="Current Semester Overview">
            <div style={{ padding: '20px' }}>
              <h2>{data.currentSemesterStats?.semesterName} ({data.currentSemesterStats?.semesterCode})</h2>
              <Row gutter={[16, 16]} style={{ marginTop: '20px' }}>
                <Col span={12}>
                  <Statistic title="Courses" value={data.currentSemesterStats?.coursesCount} />
                </Col>
                <Col span={12}>
                  <Statistic title="Enrollments" value={data.currentSemesterStats?.enrollmentsCount} />
                </Col>
                <Col span={12}>
                  <Statistic title="Teams" value={data.currentSemesterStats?.teamsCount} />
                </Col>
                <Col span={12}>
                  <Statistic 
                    title="Avg Team Size" 
                    value={data.currentSemesterStats?.averageTeamSize} 
                    precision={2}
                  />
                </Col>
              </Row>
            </div>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default AdminDashboard;
```

---

### 3. **Student Report Component**

```typescript
// components/StudentReportBySemester.tsx
import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Select, Card, Table, Row, Col, Statistic, Spin } from 'antd';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import reportApi from '@/services/reportService';

interface CourseDetail {
  courseId: number;
  courseCode: string;
  courseName: string;
  mentorName: string;
  enrolledCount: number;
  teamsCount: number;
  studentsInTeams: number;
  teamFormationRate: number;
}

interface StudentReport {
  reportType: string;
  filterName: string;
  filterValue: number;
  totalStudents: number;
  enrolledStudents: number;
  studentsInTeams: number;
  studentsWithoutTeams: number;
  courseDetails: CourseDetail[];
}

const StudentReportBySemester: React.FC = () => {
  const [selectedSemester, setSelectedSemester] = useState<number>(1);

  // Fetch semesters for dropdown
  const { data: semesters } = useQuery({
    queryKey: ['semesters'],
    queryFn: async () => {
      // Implement fetch semesters API
      return [];
    }
  });

  // Fetch report data
  const { data, isLoading } = useQuery<StudentReport>({
    queryKey: ['studentReport', 'semester', selectedSemester],
    queryFn: () => reportApi.getStudentsBySemester(selectedSemester),
    enabled: !!selectedSemester
  });

  if (isLoading) return <Spin size="large" />;
  if (!data) return null;

  const columns = [
    {
      title: 'Course Code',
      dataIndex: 'courseCode',
      key: 'courseCode',
    },
    {
      title: 'Course Name',
      dataIndex: 'courseName',
      key: 'courseName',
    },
    {
      title: 'Mentor',
      dataIndex: 'mentorName',
      key: 'mentorName',
    },
    {
      title: 'Enrolled',
      dataIndex: 'enrolledCount',
      key: 'enrolledCount',
    },
    {
      title: 'Teams',
      dataIndex: 'teamsCount',
      key: 'teamsCount',
    },
    {
      title: 'In Teams',
      dataIndex: 'studentsInTeams',
      key: 'studentsInTeams',
    },
    {
      title: 'Formation Rate',
      dataIndex: 'teamFormationRate',
      key: 'teamFormationRate',
      render: (rate: number) => `${rate.toFixed(2)}%`
    },
  ];

  return (
    <div>
      <h1>Student Report by Semester</h1>

      {/* Semester Selector */}
      <Select
        style={{ width: 300, marginBottom: '24px' }}
        placeholder="Select Semester"
        value={selectedSemester}
        onChange={setSelectedSemester}
      >
        {/* Map semesters here */}
      </Select>

      {/* Summary Cards */}
      <Row gutter={[16, 16]} style={{ marginBottom: '24px' }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic title="Total Students" value={data.totalStudents} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic title="Enrolled Students" value={data.enrolledStudents} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic 
              title="Students in Teams" 
              value={data.studentsInTeams}
              valueStyle={{ color: '#10b981' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic 
              title="Without Teams" 
              value={data.studentsWithoutTeams}
              valueStyle={{ color: '#ef4444' }}
            />
          </Card>
        </Col>
      </Row>

      {/* Team Formation Rate Chart */}
      <Card title="Team Formation Rate by Course" style={{ marginBottom: '24px' }}>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={data.courseDetails}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="courseCode" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="teamFormationRate" fill="#3b82f6" name="Formation Rate (%)" />
          </BarChart>
        </ResponsiveContainer>
      </Card>

      {/* Detailed Table */}
      <Card title="Course Details">
        <Table 
          columns={columns} 
          dataSource={data.courseDetails}
          rowKey="courseId"
          pagination={{ pageSize: 10 }}
        />
      </Card>
    </div>
  );
};

export default StudentReportBySemester;
```

---

## 📱 RESPONSIVE DESIGN

- **Mobile** (< 768px): Stack cards vertically, simplify charts
- **Tablet** (768px - 1024px): 2-column layout for cards
- **Desktop** (> 1024px): Full dashboard with side navigation

---

## 🔍 SEARCH & FILTER FEATURES

Implement filters on all report pages:
- **Semester Filter**: Dropdown with all semesters
- **Mentor Filter**: Dropdown with all mentors (for student reports)
- **Major Filter**: Dropdown with all majors (for student reports)
- **Date Range Picker**: For time-based reports (future feature)

---

## 📥 EXPORT FEATURES (Optional)

Add export buttons to download reports:
- **Export to Excel**: Use libraries like `xlsx` or `exceljs`
- **Export to PDF**: Use `jsPDF` or `react-pdf`
- **Print**: Browser print functionality

```typescript
// Example export to Excel
import * as XLSX from 'xlsx';

const exportToExcel = (data: any[], filename: string) => {
  const ws = XLSX.utils.json_to_sheet(data);
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, 'Report');
  XLSX.writeFile(wb, `${filename}.xlsx`);
};
```

---

## ✅ TESTING CHECKLIST

- [ ] Dashboard loads correctly with all statistics
- [ ] All charts render properly
- [ ] Semester selector works for student reports
- [ ] Mentor filter works correctly
- [ ] Major filter works correctly
- [ ] Tables are sortable and paginated
- [ ] Mobile responsive design works
- [ ] Export functionality works (if implemented)
- [ ] Loading states display correctly
- [ ] Error handling works properly
- [ ] API authorization (JWT token) works

---

## 🚀 DEPLOYMENT NOTES

1. **Environment Variables:**
```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
# or production URL
NEXT_PUBLIC_API_URL=https://api.yourdomain.com/api
```

2. **Build Command:**
```bash
npm run build
```

3. **Start Command:**
```bash
npm start
```

---

## 📚 ADDITIONAL RESOURCES

- **Swagger API Docs**: `http://localhost:8080/swagger-ui.html`
- **Backend Test Accounts**: See `TEST_ACCOUNTS.md`
- **API Permissions**: See `API_PERMISSIONS.md`

---

## 🎯 PRIORITY IMPLEMENTATION ORDER

1. **Phase 1 - Core Dashboard** (Day 1-2)
   - Admin login
   - Dashboard overview with statistics cards
   - Basic charts (pie chart for users)

2. **Phase 2 - Student Reports** (Day 3-4)
   - Report by semester
   - Report by mentor
   - Report by major

3. **Phase 3 - Mentor Performance** (Day 5)
   - Mentor performance report
   - Comparison charts

4. **Phase 4 - Additional Stats** (Day 6-7)
   - Course statistics
   - Team statistics
   - Enrollment statistics

5. **Phase 5 - Polish** (Day 8-9)
   - Responsive design
   - Export functionality
   - Loading/Error states
   - Testing

---

## 💡 TIPS & BEST PRACTICES

1. **Use React Query** for data fetching (automatic caching, refetching)
2. **Implement skeleton loaders** for better UX during data loading
3. **Add error boundaries** to catch and display errors gracefully
4. **Use TypeScript** for type safety
5. **Implement optimistic updates** where applicable
6. **Cache API responses** to reduce server load
7. **Add refresh button** to manually reload data
8. **Show last updated timestamp** on reports
9. **Implement breadcrumb navigation** for better UX
10. **Add tooltips** to explain metrics and charts

---

## 📞 SUPPORT

For API issues or questions:
- Check Swagger docs at `/swagger-ui.html`
- Review backend logs
- Test with Postman/Insomnia first

---

**Last Updated**: November 14, 2025  
**Version**: 1.0.0  
**Backend API Version**: Compatible with v1.0.0

---

## 🎨 FIGMA/DESIGN MOCKUPS (If Available)

[Add link to Figma designs here]

---

**Good luck with implementation! 🚀**

