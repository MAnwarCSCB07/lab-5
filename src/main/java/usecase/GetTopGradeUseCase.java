package usecase;

import api.GradeDataBase;
import entity.Grade;
import entity.Team;

/**
 * GetTopGradeUseCase class.
 */
public final class GetTopGradeUseCase {
    private final GradeDataBase gradeDataBase;

    public GetTopGradeUseCase(GradeDataBase gradeDataBase) {
        this.gradeDataBase = gradeDataBase;
    }

    /**
     * Get the highest grade for a course across your team.
     * @param course The course.
     * @return The top grade.
     */
    public float getTopGrade(String course) {
        // Call the API to get the usernames of all your team members
        float max = 0; // Assume 0 is the minimum possible grade
        final Team team = gradeDataBase.getMyTeam();

        // Iterate through all team members
        for (String username : team.getMembers()) {
            // Call the API to get all grades for the current team member
            final Grade[] grades = gradeDataBase.getGrades(username);

            // Check each grade for this member
            for (Grade grade : grades) {
                // If the grade is for the target course, check if it's the new maximum
                if (grade.getCourse().equals(course)) {
                    // Find the maximum grade
                    if (grade.getGrade() > max) {
                        max = grade.getGrade();
                    }
                }
            }
        }
        return max;
    }
}