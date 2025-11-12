package com.clup.interfaces;

import com.clup.model.*;
import java.util.*;

public interface Schedulable {

    Schedule schedule(Schedule s);

    List<Schedule> findConflicts(Schedule candidate);
}
