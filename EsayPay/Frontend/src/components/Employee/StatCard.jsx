export default function StatCard({ value, title, subtitle }) {
  return (
    <div className="col-md-3 col-sm-6">
      <div className="card h-100 shadow-sm border-0 rounded-3">
        <div className="card-body text-center p-4">
          <h2 className="display-6 fw-bold text-dark mb-2">{value}</h2>
          <h6 className="text-uppercase text-muted fw-semibold mb-1">{title}</h6>
          <small className="text-muted">{subtitle}</small>
        </div>
      </div>
    </div>
  );
}