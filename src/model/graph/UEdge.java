package model.graph;

import exceptions.InvalidEdgeException;

public record UEdge<A>(A u, A v) {
    public UEdge {
        if (u.equals(v)) {
            throw new InvalidEdgeException("Self-loops not allowed for simple graphs");
        }

        if (u.hashCode() > v.hashCode()
            || (u.hashCode() == v.hashCode() 
                && !u.equals(v) 
                && u.toString().compareTo(v.toString()) > 0)) {
            
            A temp = u;
            u = v;
            v = temp;
        }
    }

    @Override
    public String toString() {
        return "{" + u + ", " + v + "}";
    }
}
