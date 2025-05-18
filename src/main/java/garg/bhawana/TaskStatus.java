package garg.bhawana;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum TaskStatus {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");

    String displayValue;

    TaskStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public static TaskStatus from(String str) {
        final Optional<TaskStatus> status = Arrays.stream(TaskStatus.values())
                .filter(v -> v.displayValue.equals(str))
                .findAny();
        if (status.isPresent())
            return status.get();
        throw new EnumConstantNotPresentException(TaskStatus.class, str);
    }

    public static List<String> displayValues() {
        return Arrays.stream(TaskStatus.values())
                .map(v -> v.displayValue)
                .collect(Collectors.toList());
    }
}
