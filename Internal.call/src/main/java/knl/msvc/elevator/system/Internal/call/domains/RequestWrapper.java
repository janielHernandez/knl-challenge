package knl.msvc.elevator.system.Internal.call.domains;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;


@Data
@ToString
@SuperBuilder
@NoArgsConstructor
public class RequestWrapper implements Comparable<RequestWrapper>{

    private int sourceFloor;
    private TreeSet<Request> requests = new TreeSet<>();

    public void addRequest(Request request){
        this.requests.add(request);
    }
    public boolean validateIDExistence(String id) {
        return requests
                .stream()
                .anyMatch(e->e.getId().equals(id));
    }

    public Optional<Request> getRequestById(String id) {
        return requests
                .stream()
                .filter(e->e.getId().equals(id))
                .findAny();
    }

    @Override
    public int compareTo(RequestWrapper o) {
        return this.sourceFloor - o.getSourceFloor();
    }

}
