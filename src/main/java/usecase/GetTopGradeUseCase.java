package usecase;

import api.GradeDataBase;
import entity.Grade;
import entity.Team;
import org.json.JSONException; // Import for exception handling

import java.util.Objects; // Import for null-safe comparison

public final class GetTopGradeUseCase {
    private final GradeDataBase gradeDataBase;

    public GetTopGradeUseCase(GradeDataBase gradeDataBase) {
        this.gradeDataBase = gradeDataBase;
    }

    /**
     * Get the highest grade for a course across your team.
     * @param course The course.
     * @return The top grade, or 0 if no matching grades are found.
     */
    public float getTopGrade(String course) {
        // Initialize max to the lowest possible value to correctly find the
        // maximum, even if grades are negative.
        float max = Float.NEGATIVE_INFINITY;
        boolean found = false; // Flag to track if we found any matching grade

        // Call the API to get the usernames of all your team members
        final Team team = gradeDataBase.getMyTeam();

        // Check if team or members list is null before iterating
        if (team != null && team.getMembers() != null) {
            // Iterate through all team members
            for (String username : team.getMembers()) {
                // Check for null usernames
                if (username == null) continue;

                final Grade[] grades;
                try {
                    // Call the API to get all grades for the current team member
                    grades = gradeDataBase.getGrades(username);
                } catch (JSONException e) {
                    // skip this member on error, continue with others
                    continue;
                }

                // Check if the returned grades array is null
                if (grades == null) continue;

                // Check each grade for this member
                for (Grade grade : grades) {
                    // Check if the grade object itself is null
                    if (grade == null) continue;

                    // Use Objects.equals for a null-safe comparison of the course name
                    if (Objects.equals(grade.getCourse(), course)) {
                        found = true; // We found at least one matching grade
                        // Find the maximum grade
                        if (grade.getGrade() > max) {
                            max = grade.getGrade();
                        }
                    }
                }
            }
        }

        // If we found a grade, return the max.
        // Otherwise, return 0 to match the behavior of GetAverageGradeUseCase.
        return found ? max : 0;
    }
}