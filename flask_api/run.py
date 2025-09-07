from app import create_app
from  app.config import port
app = create_app()

if __name__ == '__main__':
    app.run( debug=True, port=port)
