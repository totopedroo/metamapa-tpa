package ar.edu.utn.frba.server.gestorUsuarios.domain;

import ar.edu.utn.frba.server.gestorUsuarios.domain.Rol;      // <-- ajustá estos imports al paquete real de tus enums
import ar.edu.utn.frba.server.gestorUsuarios.domain.Permiso;  // <-- si tus enums están en otro package, cambiá las rutas

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Entity
@Table(name = "usuarios",
        uniqueConstraints = @UniqueConstraint(name = "uk_usuario_nombre", columnNames = "nombre_de_usuario"))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = true, length = 120)
    private String nombre;
    @Column(name = "mail", nullable = true, length = 120)
    private String email;

    // username
    @Column(name = "nombre_de_usuario", nullable = false, length = 120)
    private String nombreDeUsuario;

    // password (BCrypt)
    @Column(name = "contrasenia", nullable = false, length = 100)
    private String contrasenia;

    // rol como enum string
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 40)
    private Rol rol;

    // permisos como ElementCollection de enums
    @Transient
    private List<Permiso> permisos = new List<Permiso>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NotNull
        @Override
        public Iterator<Permiso> iterator() {
            return null;
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NotNull
        @Override
        public <T> T[] toArray(@NotNull T[] a) {
            return null;
        }

        @Override
        public boolean add(Permiso permiso) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends Permiso> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NotNull Collection<? extends Permiso> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Permiso get(int index) {
            return null;
        }

        @Override
        public Permiso set(int index, Permiso element) {
            return null;
        }

        @Override
        public void add(int index, Permiso element) {

        }

        @Override
        public Permiso remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @NotNull
        @Override
        public ListIterator<Permiso> listIterator() {
            return null;
        }

        @NotNull
        @Override
        public ListIterator<Permiso> listIterator(int index) {
            return null;
        }

        @NotNull
        @Override
        public List<Permiso> subList(int fromIndex, int toIndex) {
            return List.of();
        }
    };

    // flags opcionales para seguridad; si no los usás, podés quitarlos o dejarlos con default
    @Column(name = "habilitado", nullable = false)
    @Builder.Default
    private boolean habilitado = true;

    @Column(name = "bloqueado", nullable = false)
    @Builder.Default
    private boolean bloqueado = false;
}
