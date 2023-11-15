public record Cube(int X, int Y, int Z) {
    public Cube Add(Cube toAdd) {
        return new Cube(this.X + toAdd.X, this.Y + toAdd.Y, this.Z + toAdd.Z);
    }

    @Override
    public String toString() {
        return X + "," + Y + "," + Z;
    }
}
