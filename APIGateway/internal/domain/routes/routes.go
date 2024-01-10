package domain

type Config struct {
	Routes []Route `yaml:"routes"`
}

type Route struct {
	Method string `yaml:"method"`
	Source   string `yaml:"source"`
	Target string `yaml:"target"`
}

type Routes interface {
    LoadConfig() (*Config, error)
}
