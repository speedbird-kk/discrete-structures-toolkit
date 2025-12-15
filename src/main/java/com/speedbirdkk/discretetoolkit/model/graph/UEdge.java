package com.speedbirdkk.discretetoolkit.model.graph;

import com.speedbirdkk.discretetoolkit.exceptions.InvalidEdgeException;

/**
 * A representation of an undirected edge on a simple undirected graph. More formally, an
 * unordered pair of objects of the same type which represent the two vertices that form
 * the ends of an undirected edge. Moreover, the two vertices are not equal as simple
 * undirected graphs do not allow self-loops.
 * 
 * <p>This class models such an undirected edge as a record with two components of the same
 * type {@code <A>}, namely {@code u} and {@code v}, which represent the two vertices that
 * form the ends of an undirected edge.
 * 
 * <p><strong>Note:</strong> To ensure undirected / unordered pair behaviour, specifically that
 * {@code new UEdge<>(u, v).equals(new UEdge<>(v, u))} returns {@code true}, the end vertices
 * {@code u} and {@code v} are stored internally in a canonical order. This is done by
 * comparing {@code hashCode()} and using {@code toString()} as a tiebreaker in case {@code hashCode()}
 * is equal for {@code u} and {@code v}. Therefore, ensure that {@code toString()} for objects of type
 * {@code <A>} are stable and deterministic.
 * 
 * @param <A> the type of vertices that form the undirected edge.
 * 
 * @author speedbird-kk
 * @see UGraph
 * @since 2025
 * @version 1.0
 */
public record UEdge<A>(A u, A v) {
    /**
     * Constructs an undirected edge between the vertices {@code u} and {@code v} both
     * of type {@code <A>}.
     * 
     * @param u End vertex of the undirected edge
     * @param v End vertex of the undirected edge
     * @throws InvalidEdgeException if {@code u.equals(v) = true} as self-loops are not allowed
     */
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
