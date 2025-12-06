package model.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import model.relational.Mapping;
import util.Sets;
import util.Validate;

/**
 * A representation of a simple undirected graph. More formally, a structure containing
 * a set {@code vertices} and a set {@code edges} such that:
 * <ul>
 *  <li>The set {@code edges} is a subset of the set {@code vertices} choose 2, which 
 *      is the set of all subsets of {@code vertices} with size 2.
 *  <li>Each edge in {@code edges} is undirected so that it can be represented as a
 *      mathematical set, or an unordered pair, and the edge {u, v} = {v, u} for any
 *      u, v in {@code vertices}.
 *  <li>There are no self-loops, so no vertex has an edge to itself. Hence for all edges
 *      {u, v} where u, v in {@code vertices}, we have that u ≠ v.
 * </ul>
 * 
 * <p>This class models the mathematical simple undirected graph in the following way:
 * <ul>
 *  <li>{@code vertices} contains elements all of type {@code A}
 *  <li>{@code edges} contains {@code UEdge} objects which are unordered pairs. Moreover,
 *      the condition of no self-loops is enforced in the construction of {@code UEdge}.
 * </ul>
 * 
 * @param <A> the type of vertices in the simple undirected graph
 * 
 * @author speedbird-kk
 * @see UEdge
 * @since 2025
 * @version 1.0
 */

public final class UGraph<A> implements Graph<A> {
    /**
     * Set of vertices in the graph with vertex type {@code A}
     */
    private final Set<A> vertices;

    /**
     * Set of edges in the graph with each edge represented as a {@code UEdge<A>} object.
     * @see UEdge
     */
    private final Set<UEdge<A>> edges;

    /**
     * Constructs a new simple undirected graph from a set of vertices and
     * undirected edges.
     * @param vertices
     * @param edges
     */
    public UGraph(Set<A> vertices, Set<UEdge<A>> edges) {
        this.vertices = Set.copyOf(vertices);
        this.edges = Set.copyOf(edges);
    }

    /**
     * Constructs a new simple undirected from a set of undirected edges.
     * The set of vertices is inferred from the set of edges, so that
     * the resulting graph is connected.
     * 
     * <p>Constructs a graph with the set of edges {@code edges} and sets the set
     * of vertices {@code vertices} so that the graph is connected. In other words, it
     * is the set of all endpoints of {@code edges}.
     * 
     * @param edges the set of all edges in the graph
     * @throws NullPointerException if {@code edges} is null
     * @return the connected graph with the edge set {@code edges}
     */
    public static <A> UGraph<A> fromEdges(Set<UEdge<A>> edges) {
        Objects.requireNonNull(edges, "Set of edges must not be null");

        Set<A> vertices = edges.stream()
            .flatMap(e -> Stream.of(e.u(), e.v()))
            .collect(Collectors.toSet());
        
        return new UGraph<>(vertices, edges);
    }

    /**
     * Constructs the complete undirected graph on the given set of vertices.
     * <p>For a set of vertices {@code V} of size {@code n}, this returns the graph
     * containing all {@code n(n-1)/2} possible unordered edges between distinct
     * vertices so that every pair of vertices is adjacent
     * @param <A> the vertex type
     * @param vertices the set of vertices V
     * @throws NullPointerException if set {@code vertices} is null
     * @throws IllegalArgumentException if set {@code vertices} contains less than 2 elements
     * @return the complete graph on {@code vertices}
     */
    public static <A> UGraph<A> complete(Set<A> vertices) {
        Objects.requireNonNull(vertices, "Set of vertices must not be null");

        if (vertices.size() < 2) {
            throw new IllegalArgumentException(
                "Set of vertices must have more than 2 elements for a complete graph");
        }

        Set<UEdge<A>> edges = Sets.choose(vertices, 2).stream()
            .map(ArrayList::new)
            .map(list -> new UEdge<>(list.get(0), list.get(1)))
            .collect(Collectors.toSet());
        
        return new UGraph<>(vertices, edges);
    }

    /**
     * Constructs the star graph with one central vertex adjacent to all other vertices
     * in a given set of vertices and a centre vertex
     * 
     * <p> For a set of vertices {@code V}, this returns the graph where the only edges are
     * the edges connecting the centre vertex to each of the other vertices

     * @param <A> the vertex type
     * @param vertices the set of vertices V
     * @param centre the centre vertex in V
     * @throws NullPointerException if provided set of vertices or centre vertex is null
     * @throws IllegalArgumentException if the provided centre vertex is not contained in
     * the set of vertices
     * @return the star graph on {@code vertices} with {@code centre} as the central vertex
     */
    public static <A> UGraph<A> star(Set<A> vertices, A centre) {
        Objects.requireNonNull(vertices, "Set of vertices must not be null");
        Objects.requireNonNull(centre, "Centre vertex must not be null");

        if (!vertices.contains(centre)) {
            throw new IllegalArgumentException("Centre vertex must be contained in the set of vertices");
        }

        Set<UEdge<A>> edges = vertices.stream()
            .filter(v -> !v.equals(centre))
            .map(v -> new UEdge<>(centre, v))
            .collect(Collectors.toSet());
        
        return new UGraph<>(vertices, edges);
    }

    /**
     * Constructs the path graph from a list {@code vertices} [v1, v2,..., vn] such that
     * there is an edge between each two consecutive vertices in the list.
     * 
     * <p>From a list of vertices [v1, v2,..., vn], this returns a graph where the set of
     * vertices is the set of the elements of the list {v1, v2,..., vn} and the set of
     * edges is {{v(i), v(i + 1)}} for i = 0, 1,..., n - 1.

     * @param <A> the vertex type
     * @param vertices the list of vertices
     * @throws NullPointerException if list {@code vertices} is null
     * @throws IllegalArgumentException if the list {@code vertices} has less than 2 elements or
     * if the list contains duplicate elements
     * @return the path graph from the list {@code vertices}
     */
    public static <A> UGraph<A> path(List<A> vertices) {
        Objects.requireNonNull(vertices, "List of vertices must not be null");

        if (!Validate.noDuplicatesInList(vertices)) {
            throw new IllegalArgumentException("List of vertices must not contain duplicate elements");
        }

        if (vertices.size() < 2) {
            throw new IllegalArgumentException("List of vertices in a path must contain at least 2 elements");
        }

        Set<UEdge<A>> edges = IntStream.range(0, vertices.size() - 1)
            .mapToObj(i -> new UEdge<>(vertices.get(i), vertices.get(i + 1)))
            .collect(Collectors.toSet());
        
        return new UGraph<>(new HashSet<>(vertices), edges);
    }

    /**
     * Constructs the cycle graph from a list {@code vertices} [v1, v2,..., vn] such that
     * there is an edge between each two consecutive vertices in the list and an edge, {vn, v1},
     * between the last vertex and the first vertex in the list.
     * 
     * <p>From a list of vertices [v1, v2,..., vn], this returns a graph where the set of
     * vertices is the set of the elements of the list {v1, v2,..., vn} and the set of
     * edges is {{v(i), v(i + 1)}} ∪ {vn, v1} for i = 0, 1,..., n - 1.
     * @param <A> the vertex type
     * @param vertices the list of vertices
     * @throws NullPointerException if list {@code vertices} is null
     * @throws IllegalArgumentException if the list {@code vertices} has less than 3 elements or
     * if the list contains duplicate elements
     * @return the cycle graph from the list {@code vertices}
     */
    public static <A> UGraph<A> cycle(List<A> vertices) {
        Objects.requireNonNull(vertices, "List of vertices must not be null");

        if (!Validate.noDuplicatesInList(vertices)) {
            throw new IllegalArgumentException("List of vertices must not contain duplicate elements");
        }

        if (vertices.size() < 3) {
            throw new IllegalArgumentException("List of vertices in a cycle must contain at least 3 elements");
        }

        Set<UEdge<A>> edges = IntStream.range(0, vertices.size())
            .mapToObj(i -> new UEdge<>(
                vertices.get(i),
                vertices.get((i + 1) % vertices.size())
            ))
            .collect(Collectors.toSet());
        
        return new UGraph<>(new HashSet<>(vertices), edges);
    }

    /**
     * Returns {@code true} if the graph contains an edge between vertex {@code v} and
     * vertex {@code u}. In other words, returns {@code true} if vertex {@code v} is
     * adjacent (a neighbour of) vertex {@code u}.
     * 
     * <p>Returns true if and only if the set of edges {@link UGraph#edges} contains the {@code UEdge}
     * with components {@code v} and {@code u}.
     * @param v
     * @param u
     * @return {@code true} if the vertex {@code v} is adjacent to vertex {@code u} in the graph
     */
    public boolean hasEdge(A v, A u) {
        return edges.contains(new UEdge<A>(u, v));
    }

    /**
     * Returns the degree of vertex {@code v}.
     * 
     * <p>Counts the number of edges that the vertex {@code v} is a part of and returns the result.
     * {@code v} is a part of an edge if the {@code UEdge} has one of its components equal to {@code v}
     * @param v the vertex
     * @throws IllegalArgumentException if {@code v} is not contained in the set of vertices
     * @return the number of edges the vertex {@code v} is a part of
     */
    public long degree(A v) {
        if (!vertices.contains(v)) {
            throw new IllegalArgumentException("Vertex must be contained in the set of vertices");
        }

        return edges.stream()
            .filter(e -> e.u().equals(v) || e.v().equals(v))
            .count();
    }

    /**
     * Returns the set of vertices that are adjacent to vertex {@code v}. In other words, returns
     * the set of neighbours, or the neighbourhood of {@code v}.
     * 
     * <p>Returns a subset of the set of all {@code vertices} in the graph such that each vertex, {@code w},
     * in the subset is adjacent to {@code v}, hence the set of edges contains the {@code UEdge} with
     * components {@code v} and {@code w}.
     * 
     * @param v the vertex
     * @throws IllegalArgumentException if {@code v} is not contained in the set of all vertices
     * @return a subset of {@code vertices} such that all vertices in the subset are adjacent to {@code v}
     */
    public Set<A> neighbours(A v) {
        if (!vertices.contains(v)) {
            throw new IllegalArgumentException("Vertex must be contained in the set of vertices");
        }

        return edges.stream()
            .filter(e -> e.u().equals(v) || e.v().equals(v))
            .flatMap(e -> Stream.of(e.u(), e.v()))
            .filter(w -> !w.equals(v))
            .collect(Collectors.toSet());
    }

    /**
     * Returns the number of incidences in the graph.
     * 
     * <p>Returns the number of all possible ordered pairs (v, e) in the graph where v is a
     * vertex in {@code vertices} and e is an edge in {@code edges}. This number corresponds
     * to twice the size of the set {@code edges}, or the sum of the degrees of all vertices.
     * @see #degree(Object)
     * @return the number of incidences in the graph
     */
    public int incidencesCount() {
        return 2 * edges.size();
    }

    /**
     * Returns the number of edges in the graph.
     * 
     * <p>Returns the size of the set {@code edges}.
     * @return the number of edges in the graph
     */
    public int edgesCount() {
        return edges.size();
    }

    /**
     * Returns the number of vertices in the graph.
     * 
     * <p>Returns the size of the set {@code vertices}.
     * @return the number of vertices in the graph
     */
    public int verticesCount() {
        return vertices.size();
    }

    /**
     * Returns the non-increasing degree sequence of the graph as a list.
     * 
     * <p>For each vertex in {@code vertices}, this finds the degree of the vertex
     * and adds the result to a list that is sorted in reverse order so that the
     * result is a non-increasing sequence of degrees of all vertices in the graph.
     * @see #degree(Object)
     * @return the non-increasing degree sequence of the graph as a list
     */
    public List<Long> degreeSequence() {
        return vertices.stream()
            .map(this::degree)
            .sorted(Collections.reverseOrder())
            .toList();
    }

    /**
     * Returns a new {@code UGraph} without the specified {@code edge}.
     * 
     * <p>Returns a new {@code UGraph} with the same set {@code vertices} and the set of edges
     * as {@code edges - edge}, thus all edges in {@code edges} except for the specified {@code edge}
     * to remove.
     * @param edge the edge to remove in the new graph
     * @throws IllegalArgumentException if {@code edge} is not contained in the set {@code edges}
     * @return the new {@code UGraph} without the specified {@code edge}
     */
    public UGraph<A> removeEdge(UEdge<A> edge) {
        if (!edges.contains(edge)) {
            throw new IllegalArgumentException("Edge to remove must be contained in the set of edges");
        }
        
        return new UGraph<>(vertices, Sets.difference(edges, Set.of(edge)));
    }

    /**
     * Returns a new {@code UGraph} with the specified {@code edge} added.
     * 
     * <p>Returns a new {@code UGraph} with the same set {@code vertices} and the set of edges
     * as {@code edges ∪ edge}, thus the set containing all edges in {@code edges} and the
     * specified {@code edge} to add.
     * @param edge
     * @throws IllegalArgumentException if the specified {@code edge} is between vertices that are
     * not contained in the set {@code vertices}
     * @return the new {@code UGraph} with the specified {@code edge} added.
     */
    public UGraph<A> addEdge(UEdge<A> edge) {
        if (!(vertices.contains(edge.u()) && vertices.contains(edge.v()))) {
            throw new IllegalArgumentException("Edge must be between vertices contained in the set of vertices");
        }

        return new UGraph<>(vertices, Sets.union(edges, Set.of(edge)));
    }

    /**
     * Returns the set of vertices in the simple undirected graph.
     * @return the set of vertices
     */
    public Set<A> vertices() {
        return vertices;
    }

    /**
     * Returns the set of edges in the simple undirected graph.
     * @return the set of edges
     */
    public Set<UEdge<A>> edges() {
        return edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UGraph<?>)) return false;

        UGraph<?> other = (UGraph<?>) o;

        return this.vertices.equals(other.vertices)
            && this.edges.equals(other.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, edges);
    }

    @Override
    public String toString() {
        return "(Vertices = " + vertices + "\n"
            + "Edges = " + edges + ")";
    }
}