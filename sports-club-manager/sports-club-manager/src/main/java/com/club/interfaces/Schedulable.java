
package com.club.interfaces;

import com.club.model.Schedule;
import java.util.*;

public interface Schedulable {
    Schedule schedule(Schedule s);
    List<Schedule> findConflicts(Schedule candidate);
}
